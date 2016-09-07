package com.fr.design.extra;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.FRLogger;
import com.fr.general.SiteCenter;
import javafx.scene.web.WebEngine;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * Created by lp on 2016/8/10.
 */
public class QQLoginWebBridge {

    private static com.fr.design.extra.QQLoginWebBridge helper;
    private WebEngine webEngine;
    private static String LOGINSUCCESS = "ok";
    private static String LOGINFAILED = "failed";
    private UIDialog uiDialog;
    private UILabel uiLabel;
    private UILabel pluginuiLabel;
    private UIDialog qqDialog;
    private String username;


    private QQLoginWebBridge() {
    }

    public static com.fr.design.extra.QQLoginWebBridge getHelper() {
        if (helper != null) {
            return helper;
        }
        synchronized (com.fr.design.extra.QQLoginWebBridge.class) {
            if (helper == null) {
                helper = new com.fr.design.extra.QQLoginWebBridge();
            }
            return helper;
        }
    }

    public void setEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public void setDialogHandle(UIDialog uiDialog) {
        this.uiDialog = uiDialog;
    }

    public void setQQDialogHandle(UIDialog uiDialog) {
        this.qqDialog = uiDialog;
    }

    public void setUILabel(UILabel uiLabel) {
        this.uiLabel = uiLabel;
    }

    public void setUILabelInPlugin(UILabel uiLabel) {
        this.pluginuiLabel = uiLabel;
    }

    public void setLoginlabel() {
        username = DesignerEnvManager.getEnvManager().getBBSName();
    }

    public static com.fr.design.extra.QQLoginWebBridge getHelper(WebEngine webEngine) {
        getHelper();
        helper.setEngine(webEngine);
        return helper;
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
     * 关闭父窗口
     */
    public void closeParentWindow() {
        if (uiDialog != null) {
            uiDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            uiDialog.setVisible(false);
        }
    }

    /**
     * 获取用户信息
     * @param userInfo
     */
    public void getLoginInfo(String userInfo) {
        JSONObject jo = new JSONObject(userInfo);
        String status = jo.get("status").toString();
        if (status.equals(LOGINSUCCESS)) {
            String username = jo.get("username").toString();
            closeQQWindow();
            closeParentWindow();
            pluginuiLabel.setText(username);
            DesignerEnvManager.getEnvManager().setBBSName(username);
        }else if (status.equals(LOGINFAILED)){
            //账号没有QQ授权
            closeQQWindow();
            try {
                Desktop.getDesktop().browse(new URI(SiteCenter.getInstance().acquireUrlByKind("bbs.default")));
            }catch (Exception exp) {
            }
        }
    }

    public void openUrlAtLocalWebBrowser(WebEngine eng, String url) {
        if (url.indexOf("qqLogin.html") > 0) {
            return;
        }
        if (Desktop.isDesktopSupported()) {
            try {
                //创建一个URI实例,注意不是URL
                URI uri = URI.create(url);
                //获取当前系统桌面扩展
                Desktop desktop = Desktop.getDesktop();
                //判断系统桌面是否支持要执行的功能
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    //获取系统默认浏览器打开链接
                    desktop.browse(uri);
                }
            } catch (NullPointerException e) {
                //此为uri为空时抛出异常
                FRLogger.getLogger().error(e.getMessage());
            } catch (IOException e) {
                //此为无法获取系统默认浏览器
                FRLogger.getLogger().error(e.getMessage());
            }
        }
    }
}
