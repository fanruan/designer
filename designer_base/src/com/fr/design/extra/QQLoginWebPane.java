package com.fr.design.extra;

import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.SiteCenter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import javax.swing.*;

/**
 * Created by zhaohehe on 16/7/28.
 */
public class QQLoginWebPane extends JFXPanel {

    private WebEngine webEngine;

    public QQLoginWebPane(final String installHome) {
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BorderPane root = new BorderPane();
                Scene scene = new Scene(root);
                QQLoginWebPane.this.setScene(scene);
                WebView webView = new WebView();
                webEngine = webView.getEngine();
                webEngine.load("file:///" + installHome + "/scripts/qqLogin/web/qqLogin.html");
                webEngine.locationProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, final String oldValue, String newValue) {
                        disableLink(webEngine);
                        // webView好像默认以手机版显示网页，浏览器里过滤掉这个跳转
                        if (ComparatorUtils.equals(newValue, "file:///" + installHome + "/scripts/qqLogin/web/qqLogin.html") || ComparatorUtils.equals(newValue, SiteCenter.getInstance().acquireUrlByKind("bbs.mobile"))) {
                            return;
                        }
                        QQLoginWebBridge.getHelper().openUrlAtLocalWebBrowser(webEngine, newValue);
                    }
                });
                webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                    @Override
                    public void handle(WebEvent<String> event) {
                        showAlert(event.getData());
                    }
                });
                JSObject obj = (JSObject) webEngine.executeScript("window");
                obj.setMember("QQLoginHelper", QQLoginWebBridge.getHelper(webEngine));
                webView.setContextMenuEnabled(false);//屏蔽右键
                root.setCenter(webView);
            }
        });
    }

    private void showAlert(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(QQLoginWebPane.this, message);
            }
        });
    }

    private void disableLink(final WebEngine eng) {
        try {
            // webView端不跳转 虽然webView可以指定本地浏览器打开某个链接，但是当本地浏览器跳转到指定链接的同时，webView也做了跳转，
            // 为了避免出现在一个600*400的资讯框里加载整个网页的情况，webView不跳转到新网页
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    eng.executeScript("location.reload()");
                    QQLoginWebBridge.getHelper().closeQQWindow();
                }
            });
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
    }
}