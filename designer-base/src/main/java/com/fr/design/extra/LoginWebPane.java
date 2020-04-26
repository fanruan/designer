package com.fr.design.extra;

import com.fr.design.dialog.FineJOptionPane;
import javafx.application.Platform;
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
 * Created by zhaohehe on 16/7/26.
 */
public class LoginWebPane extends JFXPanel {

    public LoginWebPane(final String installHome) {
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BorderPane root = new BorderPane();
                Scene scene = new Scene(root);
                LoginWebPane.this.setScene(scene);
                WebView webView = new WebView();
                WebEngine webEngine = webView.getEngine();
                webEngine.load("file:///" + installHome + "/scripts/login.html");
                webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                    @Override
                    public void handle(WebEvent<String> event) {
                        showAlert(event.getData());
                    }
                });
                JSObject obj = (JSObject) webEngine.executeScript("window");
                obj.setMember("LoginHelper", LoginWebBridge.getHelper(webEngine));
                webView.setContextMenuEnabled(false);//屏蔽右键
                root.setCenter(webView);
            }
        });
    }

    private void showAlert(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FineJOptionPane.showMessageDialog(LoginWebPane.this, message);
            }
        });
    }
}
