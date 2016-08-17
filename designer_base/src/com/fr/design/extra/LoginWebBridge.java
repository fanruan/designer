package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.FRLogger;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StringUtils;
import javafx.scene.web.WebEngine;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import javax.swing.*;
import java.awt.*;
import java.net.URLEncoder;

/**
 * Created by zhaohehe on 16/8/1.
 */
public class LoginWebBridge {

    private static final String LOGIN_SUCCESS_FLAG = "http://bbs.finereport.com";
    private static final int TIME_OUT = 10000;

    private static com.fr.design.extra.LoginWebBridge helper;
    private UIDialog uiDialog;
    private UILabel uiLabel;

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

    private LoginWebBridge() {
    }

    public void setEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public void setDialogHandle(UIDialog uiDialog) {
        this.uiDialog = uiDialog;
    }

    public void setUILabel(UILabel uiLabel) {
        this.uiLabel = uiLabel;
    }

    /**
     * 注册页面
     */
    public void registerHref() {
        try {
            Desktop.getDesktop().browse(new URI(SiteCenter.getInstance().acquireUrlByKind("bbs.default")));
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
     * @param callback
     * @return
     */
    public String defaultLogin(String username, String password) {
        if (!StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(password)) {
            //用户名密码为空
            return "-1";
        }
        if (!testConnection()) {
            //网络测试连接不通过
            return "-2";
        }
        if (login(username, password)) {
            updateUserInfo(username, password);
            loginSuccess(username);
            return "0";
        }else {
            return "-3";
        }
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

    public void updateUserInfo(String username,String password) {
        DesignerEnvManager.getEnvManager().setBBSName(username);
        DesignerEnvManager.getEnvManager().setBBSPassword(password);
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