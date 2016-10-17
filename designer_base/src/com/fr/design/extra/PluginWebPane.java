package com.fr.design.extra;

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
 * Created by richie on 16/3/19.
 */
public class PluginWebPane extends JFXPanel {

    private WebEngine webEngine;

    public PluginWebPane(final String mainJs) {
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BorderPane root = new BorderPane();
                Scene scene = new Scene(root);
                PluginWebPane.this.setScene(scene);
                WebView webView = new WebView();
                webEngine = webView.getEngine();
                webEngine.load("file:///" + mainJs);
                webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                    @Override
                    public void handle(WebEvent<String> event) {
                        showAlert(event.getData());
                    }
                });
                JSObject obj = (JSObject) webEngine.executeScript("window");
                obj.setMember("PluginHelper", PluginWebBridge.getHelper(webEngine));
                webView.setContextMenuEnabled(false);//屏蔽右键
                root.setCenter(webView);
            }
        });
    }

    private void showAlert(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(PluginWebPane.this, message);
            }
        });
    }
}
