package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.config.MarketConfig;
import com.fr.design.bbs.BBSLoginUtils;
import com.fr.design.dialog.UIDialog;
import com.fr.design.extra.exe.PluginLoginExecutor;
import com.fr.design.extra.ucenter.Client;
import com.fr.design.extra.ucenter.XMLHelper;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONObject;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StringUtils;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Desktop;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * @author vito
 */
public class LoginWebBridge {

    //默认查询消息时间, 30s
    private static final long CHECK_MESSAGE_TIME = 30 * 1000L;
    //数据查询正常的标志 ok
    private static final String SUCCESS_MESSAGE_STATUS = "ok";
    //数据通讯失败
    private static final String FAILED_MESSAGE_STATUS = "error";
    //最低消息的条数
    private static final int MIN_MESSAGE_COUNT = 0;
    //登录成功
    private static final String LOGININ = "0";
    //用户名不存在
    private static final String USERNAME_NOT_EXSIT = "-1";
    //密码错误
    private static final String PASSWORD_ERROR = "-2";
    //未知错误
    private static final String UNKNOWN_ERROR = "-3";
    //网络连接失败
    private static final String NET_FAILED = "-4";
    //用户名，密码为空
    private static final String LOGIN_INFO_EMPTY = "-5";
    private static final int TIME_OUT = 10000;
    private static final String LOGIN_SUCCESS = "ok";
    private static final String LOGIN_FAILED = "failed";
    private static final Color LOGIN_BACKGROUND = new Color(184, 220, 242);
    private static LoginWebBridge helper;
    //消息条数
    private int messageCount;
    private UIDialog uiDialog;
    private UIDialog qqDialog;
    private UILabel uiLabel;
    private WebEngine webEngine;

    private LoginWebBridge() {
    }

    public static LoginWebBridge getHelper() {
        if (helper != null) {
            return helper;
        }
        synchronized (LoginWebBridge.class) {
            if (helper == null) {
                helper = new LoginWebBridge();
            }
            return helper;
        }
    }

    public static LoginWebBridge getHelper(WebEngine webEngine) {
        getHelper();
        helper.setWebEngine(webEngine);
        return helper;
    }

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public int getMessageCount() {
        return messageCount;
    }

    /**
     * 设置获取的消息长度，并设置显示
     *
     * @param count
     */
    public void setMessageCount(int count) {
        if (count == MIN_MESSAGE_COUNT) {
            uiLabel.setText(MarketConfig.getInstance().getBbsUsername());
            MarketConfig.getInstance().setInShowBBsName(MarketConfig.getInstance().getBbsUsername());
            return;
        }
        this.messageCount = count;
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.BLANK).append(MarketConfig.getInstance().getBbsUsername())
                .append("(").append(this.messageCount)
                .append(")").append(StringUtils.BLANK);
        MarketConfig.getInstance().setInShowBBsName(sb.toString());
        uiLabel.setText(sb.toString());
    }

    public void setQQDialog(UIDialog qqDialog) {
        closeQQWindow();
        this.qqDialog = qqDialog;
    }

    public void setDialogHandle(UIDialog uiDialog) {
        closeWindow();
        this.uiDialog = uiDialog;
    }

    public void setUILabel(UILabel uiLabel) {
        this.uiLabel = uiLabel;
    }

    /**
     * 测试论坛网络连接
     *
     * @return
     */
    private boolean testConnection() {
        HttpClient client = new HttpClient(CloudCenter.getInstance().acquireUrlByKind("bbs.test"));
        return client.isServerAlive();
    }

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, EncodeConstants.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    private void sleep(long millis) {
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
            Desktop.getDesktop().browse(new URI(CloudCenter.getInstance().acquireUrlByKind("bbs.register")));
        } catch (Exception e) {
            FRContext.getLogger().info(e.getMessage());
        }
    }

    /**
     * 忘记密码
     */
    public void forgetHref() {
        try {
            Desktop.getDesktop().browse(new URI(CloudCenter.getInstance().acquireUrlByKind("bbs.reset")));
        } catch (Exception e) {
            FRContext.getLogger().info(e.getMessage());
        }
    }

    /**
     * 设计器端的用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录信息标志
     */
    public void defaultLogin(String username, String password, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new PluginLoginExecutor(username, password));
        new Thread(task).start();
    }

    /**
     * 登录操作
     *
     * @param userInfo 登录信息
     * @param password 密码
     * @return 登录信息标志
     */
    public String login(String userInfo, String password) {
        if (!StringUtils.isNotBlank(userInfo) && !StringUtils.isNotBlank(password)) {
            return LOGIN_INFO_EMPTY;
        }
        if (!testConnection()) {
            return NET_FAILED;
        }
        List<String> loginResult = frPassport(userInfo, password);
        String uid = loginResult.get(0);
        String username = loginResult.get(1);
        if (Integer.parseInt(uid) > 0) {
            loginSuccess(username);
        }
        return uid;
    }

    private List<String> frPassport(String username, String password) {
        LinkedList<String> list = new LinkedList<>();
        try {
            Client uc = new Client();
            String result = uc.ucUserLogin(username, password);
            result = new String(result.getBytes("iso-8859-1"), "gbk");
            list = XMLHelper.ucUnserialize(result);
            if (list.size() > 0) {
                int uid = Integer.parseInt(list.get(0));
                if (uid > 0) {
                    BBSLoginUtils.bbsLogin(list);
                }
            } else {
                list.push(NET_FAILED);
            }
        } catch (Exception e) {
            FRContext.getLogger().info(e.getMessage());
            list.push(UNKNOWN_ERROR);
        }
        return list;
    }

    /**
     * 关闭窗口
     */
    public void closeWindow() {
        if (uiDialog != null) {
            uiDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            uiDialog.setVisible(false);
        }
    }

    /**
     * 关闭窗口并且重新赋值
     *
     * @param username 用户名
     */
    private void loginSuccess(String username) {
        closeWindow();
        uiLabel.setText(username);
        uiLabel.setBackground(LOGIN_BACKGROUND);
    }

    /**
     * 弹出QQ授权页面
     */
    public void showQQ() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WebViewDlgHelper.createQQLoginDialog();
            }
        });
    }

    /**
     * 关闭QQ授权窗口
     */
    public void closeQQWindow() {
        if (qqDialog != null) {
            qqDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            qqDialog.setVisible(false);
        }
    }

    /**
     * 获取用户信息
     *
     * @param userInfo
     */
    public void getLoginInfo(String userInfo) {
        try {
            JSONObject jo = new JSONObject(userInfo);
            String status = jo.get("status").toString();
            if (ComparatorUtils.equals(status, LOGIN_SUCCESS)) {
                String username = jo.get("username").toString();
                int uid = Integer.parseInt(jo.get("uid") == null ? StringUtils.EMPTY : jo.get("uid").toString());
                closeQQWindow();
                loginSuccess(username);

                LinkedList<String> list = new LinkedList<>();
                list.add(String.valueOf(uid));
                list.add(username);
                list.add(StringUtils.EMPTY);
                BBSLoginUtils.bbsLogin(list);
            } else if (ComparatorUtils.equals(status, LOGIN_FAILED)) {
                //账号没有QQ授权
                closeQQWindow();
                try {
                    Desktop.getDesktop().browse(new URI(CloudCenter.getInstance().acquireUrlByKind("QQ_binding")));
                } catch (Exception ignored) {
                    // ignored
                }
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }

    public void openUrlAtLocalWebBrowser(WebEngine eng, String url) {
        if (url.indexOf("qqLogin.html") > 0) {
            return;
        }
    }
}