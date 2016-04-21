package com.fr.design.extra;

import com.fr.general.FRLogger;
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
 * Created by richie on 16/3/19.
 */
public class PluginWebPane extends JFXPanel {

    private WebEngine webEngine;

    public PluginWebPane(final String installHome) {
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BorderPane root = new BorderPane();
                Scene scene = new Scene(root);
                PluginWebPane.this.setScene(scene);
                WebView webView = new WebView();
                webEngine = webView.getEngine();
                webEngine.load("file:///" + installHome + "/scripts/store/web/index.html");
                webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                    @Override
                    public void handle(WebEvent<String> event) {
                        showAlert(event.getData());
                    }
                });
                webEngine.locationProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, final String oldValue, String newValue) {
                        disableLink(webEngine);
                        PluginWebBridge.getHelper().openUrlAtLocalWebBrowser(webEngine, newValue);
                    }
                });
                JSObject obj = (JSObject) webEngine.executeScript("window");
                obj.setMember("PluginHelper", PluginWebBridge.getHelper(webEngine));
                root.setCenter(webView);
            }
        });
    }

    private void disableLink(final WebEngine webEngine) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    webEngine.executeScript("history.go(0)");
                }
            });
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
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
