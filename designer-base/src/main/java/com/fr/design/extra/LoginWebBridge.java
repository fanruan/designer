package com.fr.design.extra;

import com.fr.base.passport.FinePassportManager;
import com.fr.concurrent.NamedThreadFactory;
import com.fr.config.MarketConfig;
import com.fr.design.dialog.UIDialog;
import com.fr.design.extra.exe.PluginLoginExecutor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.CloudCenter;
import com.fr.general.http.HttpClient;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author vito
 */
public class LoginWebBridge {

    //最低消息的条数
    private static final int MIN_MESSAGE_COUNT = 0;
    //网络连接失败
    private static final String NET_FAILED = "-4";
    //用户名，密码为空
    private static final String LOGIN_INFO_EMPTY = "-5";
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

    /**
     * 注册页面
     */
    public void registerHref() {
        try {
            Desktop.getDesktop().browse(new URI(CloudCenter.getInstance().acquireUrlByKind("bbs.register")));
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * 忘记密码
     */
    public void forgetHref() {
        try {
            Desktop.getDesktop().browse(new URI(CloudCenter.getInstance().acquireUrlByKind("bbs.reset")));
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
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
        ExecutorService es = Executors.newSingleThreadExecutor(new NamedThreadFactory("bbsDefaultLogin"));
        es.submit(task);
        es.shutdown();
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
        int uid = 0;
        try {
            uid = FinePassportManager.getInstance().login(userInfo, password);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        if (uid > 0) {
            loginSuccess(MarketConfig.getInstance().getBbsUsername());
        }
        return String.valueOf(uid);
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

    public void openUrlAtLocalWebBrowser(WebEngine eng, String url) {
        // do nothing
    }
}