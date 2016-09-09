package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.FRLogger;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONObject;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StringUtils;
import javafx.scene.web.WebEngine;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import javax.swing.*;
import java.awt.*;
import java.net.URLEncoder;
import java.util.HashMap;

public class LoginWebBridge {

    //默认查询消息时间, 30s
    private static final long CHECK_MESSAGE_TIME = 30 * 1000L;
    //数据查询正常的标志 ok
    private static final String SUCCESS_MESSAGE_STATUS = "ok";
    //数据通讯失败
    private static final String FAILED_MESSAGE_STATUS = "error";

    //消息条数
    private int messageCount;

    //最低消息的条数
    private static final int MIN_MESSAGE_COUNT = 0;

    private static final String LOGIN_SUCCESS_FLAG = "http://bbs.finereport.com";
    private static final String LOGININ = "0";
    private static final String LOGIN_INFO_EMPTY = "-1";
    private static final String DISCONNECTED = "-2";
    private static final String LOGININFO_ERROR = "-3";
    private static final int TIME_OUT = 10000;

    private static com.fr.design.extra.LoginWebBridge helper;
    private UIDialog uiDialog;
    private UILabel uiLabel;
    private String userName;

    public int getMessageCount() {
        return messageCount;
    }

    private boolean testConnection() {
        HttpClient client = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("bbs.test"));
        return client.isServerAlive();
    }

    public static com.fr.design.extra.LoginWebBridge getHelper() {
        if (helper != null) {
                return helper;
        }
        synchronized (com.fr.design.extra.LoginWebBridge.class) {
            if (helper == null) {
                    helper = new com.fr.design.extra.LoginWebBridge();
            }
            return helper;
        }
    }

    public static com.fr.design.extra.LoginWebBridge getHelper(WebEngine webEngine) {
        getHelper();
        helper.setEngine(webEngine);
        return helper;
    }

    private WebEngine webEngine;

    public void setEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public void setDialogHandle(UIDialog uiDialog) {
        this.uiDialog = uiDialog;
    }

    public void setUILabel(UILabel uiLabel) {
        this.uiLabel = uiLabel;
    }

    public LoginWebBridge() {
        String username = DesignerEnvManager.getEnvManager().getBBSName();
        setUserName(username, uiLabel);
    }

    public void setUserName(String userName, UILabel label) {
        if (uiLabel == null) {
            this.uiLabel = label;
        }
        if(StringUtils.isEmpty(userName)){
            return;
        }

        if(!StringUtils.isEmpty(this.userName)){
            updateMessageCount();
        }
        //往designerenvmanger里写一下
        DesignerEnvManager.getEnvManager().setBBSName(userName);
        this.userName = userName;
    }

    private void updateMessageCount(){
        //启动获取消息更新的线程
        //登陆状态, 根据存起来的用户名密码, 每1分钟发起一次请求, 更新消息条数.
        Thread updateMessageThread = new Thread(new Runnable() {

            @Override
            public void run() {
                sleep(CHECK_MESSAGE_TIME);
                //从env中获取username, 因为如果注销的话, env的里username会被清空.
                while(StringUtils.isNotEmpty(DesignerEnvManager.getEnvManager().getBBSName())){
                    HashMap<String, String> para = new HashMap<String, String>();
                    para.put("username", encode(encode(userName)));
                    HttpClient getMessage = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("bbs.message"), para);
                    getMessage.asGet();
                    if(getMessage.isServerAlive()){
                        try {
                            String res = getMessage.getResponseText();
                            if (res.equals(FAILED_MESSAGE_STATUS)) {
                            }else {
                                JSONObject jo = new JSONObject(res);
                                if (jo.getString("status").equals(SUCCESS_MESSAGE_STATUS)) {
                                    setMessageCount(Integer.parseInt(jo.getString("message")));
                                }
                            }
                        } catch (Exception e) {
                            FRContext.getLogger().info(e.getMessage());
                        }
                    }
                    sleep(CHECK_MESSAGE_TIME);
                }
            }
        });
        updateMessageThread.start();
    }

    public void setMessageCount(int count) {
        if (count == MIN_MESSAGE_COUNT) {
            uiLabel.setText(DesignerEnvManager.getEnvManager().getBBSName());
            DesignerEnvManager.getEnvManager().setInShowBBsName(DesignerEnvManager.getEnvManager().getBBSName());
            return;
        }
        this.messageCount = count;
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.BLANK).append(this.userName)
                .append("(").append(this.messageCount)
                .append(")").append(StringUtils.BLANK);
        DesignerEnvManager.getEnvManager().setInShowBBsName(sb.toString());
        uiLabel.setText(sb.toString());
    }

    private String encode(String str){
        try {
            return URLEncoder.encode(str, EncodeConstants.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }

    /**
     * 注册页面
     */
    public void registerHref() {
        try {
            Desktop.getDesktop().browse(new URI(SiteCenter.getInstance().acquireUrlByKind("bbs.register")));
        }catch (Exception e) {
            FRContext.getLogger().info(e.getMessage());
        }
    }

    /**
     * 忘记密码
     */
    public void forgetHref() {
        try {
            Desktop.getDesktop().browse(new URI(SiteCenter.getInstance().acquireUrlByKind("bbs.default")));
        }catch (Exception e) {
            FRContext.getLogger().info(e.getMessage());
        }
    }

    /**
     * 登录操作的回调
     * @param username
     * @param password
     * @return
     */
    public String defaultLogin(String username, String password) {
        if (!StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(password)) {
            return LOGIN_INFO_EMPTY;
        }
        if (!testConnection()) {
            return DISCONNECTED;
        }
        if (login(username, password)) {
            updateUserInfo(username, password);
            loginSuccess(username);
            setUserName(username, uiLabel);
            return LOGININ;
        }else {
            return LOGININFO_ERROR;
        }
    }

    /*
    插件管理那边的登录
     */
    public String pluginManageLogin(String username, String password, UILabel uiLabel) {
        if (!StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(password)) {
            return LOGIN_INFO_EMPTY;
        }
        if (!testConnection()) {
            return DISCONNECTED;
        }
        if (login(username, password)) {
            updateUserInfo(username, password);
            uiLabel.setText(username);
            return LOGININ;
        }else {
            return LOGININFO_ERROR;
        }
    }

    /**
     * 关闭窗口
     */
    public void closeWindow() {
        if (uiDialog != null) {
            uiDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            uiDialog.setVisible(false);
            uiDialog.dispose();
        }
    }

    public void updateUserInfo(String username,String password) {
        DesignerEnvManager.getEnvManager().setBBSName(username);
        DesignerEnvManager.getEnvManager().setBBSPassword(password);
        DesignerEnvManager.getEnvManager().setInShowBBsName(username);
        this.userName = username;
    }

    /**
     * 关闭窗口并且重新赋值
     * @param username
     */
    public void loginSuccess(String username) {
        closeWindow();
        uiLabel.setText(username);
    }

    /**
     * 弹出QQ授权页面
     */
    public void showQQ() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //弹出qq登录的窗口
                QQLoginPane managerPane = new QQLoginPane();
                UIDialog qqlog = new QQLoginDialog(DesignerContext.getDesignerFrame(),managerPane);
                QQLoginWebBridge.getHelper().setDialogHandle(uiDialog);
                QQLoginWebBridge.getHelper().setQQDialogHandle(qqlog);
                QQLoginWebBridge.getHelper().setUILabel(uiLabel);
                qqlog.setVisible(true);
            }
        });
    }

    public boolean login(String username, String password) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            try {
                username = URLEncoder.encode(username, EncodeConstants.ENCODING_GBK);
                password = URLEncoder.encode(password, EncodeConstants.ENCODING_GBK);
            } catch (UnsupportedEncodingException e) {
                FRLogger.getLogger().error(e.getMessage());
            }
            String url = SiteCenter.getInstance().acquireUrlByKind("bbs.login") + "&username=" + username + "&password=" + password;
            HttpClient client = new HttpClient(url);
            client.setTimeout(TIME_OUT);
            if (client.getResponseCodeNoException() == HttpURLConnection.HTTP_OK) {
                try {
                    String res = client.getResponseText(EncodeConstants.ENCODING_GBK);
                    if (res.contains(LOGIN_SUCCESS_FLAG)) {
                        return true;
                    }
                } catch (Exception e) {
                    FRLogger.getLogger().error(e.getMessage());
                }
            }
        }
        return false;
    }
}