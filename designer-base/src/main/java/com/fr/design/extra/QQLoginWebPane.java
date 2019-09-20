package com.fr.design.extra;

import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import netscape.javascript.JSObject;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Toolkit;

/**
 * Created by zhaohehe on 16/7/28.
 */
public class QQLoginWebPane extends JFXPanel {

    private WebEngine webEngine;
    private String url;

    private static JSObject window;

    private static int DEFAULT_PRIMARYSTAGE_WIDTH = 100;
    private static int DEFAULT_PRIMARYSTAGE_HEIGHT = 100;

    private static int DEFAULT_CONFIRM_WIDTH = 450;
    private static int DEFAULT_CONFIRM_HEIGHT = 160;
    private static int DEFAULT_OFFEST = 20;

    class Delta {
        double x, y;
    }

    public QQLoginWebPane(final String installHome) {
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BorderPane root = new BorderPane();
                Scene scene = new Scene(root);
                QQLoginWebPane.this.setScene(scene);
                final WebView webView = new WebView();
                webEngine = webView.getEngine();
                url = "file:///" + installHome + "/scripts/qqLogin.html";
                webEngine.load(url);

                final Stage primaryStage = new Stage();

                HBox layout = new HBox();
                try {
                    primaryStage.initStyle(StageStyle.TRANSPARENT);
                    primaryStage.setScene(new Scene(layout));
                    webView.getScene().getStylesheets().add(IOUtils.getResource("modal-dialog.css", getClass()).toExternalForm());
                    primaryStage.initStyle(StageStyle.UTILITY);
                    primaryStage.setScene(new Scene(new Group(), DEFAULT_PRIMARYSTAGE_WIDTH, DEFAULT_PRIMARYSTAGE_HEIGHT));
                    primaryStage.setX(0);
                    primaryStage.setY(Screen.getPrimary().getBounds().getHeight() + DEFAULT_PRIMARYSTAGE_HEIGHT);
                    primaryStage.show();
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().info(e.getMessage());
                }
                webEngine.setConfirmHandler(new Callback<String, Boolean>() {
                    @Override
                    public Boolean call(String msg) {
                        Boolean confirmed = confirm(primaryStage, msg, webView);
                        return confirmed;
                    }
                });
                configWebEngine();
                webView.setContextMenuEnabled(false);//屏蔽右键
                root.setCenter(webView);
            }
        });
    }

    private void configWebEngine() {

        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, final String oldValue, String newValue) {
                disableLink(webEngine);
                // webView好像默认以手机版显示网页，浏览器里过滤掉这个跳转
                if (ComparatorUtils.equals(newValue, url) || ComparatorUtils.equals(newValue, CloudCenter.getInstance().acquireUrlByKind("bbs.mobile"))) {
                    return;
                }
                LoginWebBridge.getHelper().openUrlAtLocalWebBrowser(webEngine, newValue);
            }
        });

        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> event) {
                showAlert(event.getData());
            }
        });

        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            window = (JSObject) webEngine.executeScript("window");
                            window.setMember("QQLoginHelper", LoginWebBridge.getHelper());
                        }
                    }
                }
        );
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
                    LoginWebBridge.getHelper().closeQQWindow();
                }
            });
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private Boolean confirm(final Stage parent, String msg, final WebView webView) {
        final BooleanProperty confirmationResult = new SimpleBooleanProperty();
        // initialize the confirmation dialog
        final Stage dialog = new Stage(StageStyle.UTILITY);
        dialog.setX(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - DEFAULT_CONFIRM_WIDTH / 2.0D + DEFAULT_OFFEST);
        dialog.setY(Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 + DEFAULT_OFFEST);
        dialog.setHeight(DEFAULT_CONFIRM_HEIGHT);
        dialog.setWidth(DEFAULT_CONFIRM_WIDTH);
        dialog.setIconified(false);
        dialog.initOwner(parent);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setScene(
                new Scene(
                        HBoxBuilder.create().styleClass("modal-dialog").children(
                                LabelBuilder.create().text(msg).build(),
                                ButtonBuilder.create().text(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_BBSLogin_Switch_Account")).defaultButton(true).onAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        // take action and close the dialog.
                                        confirmationResult.set(true);
                                        webView.getEngine().reload();
                                        dialog.close();
                                    }
                                }).build(),
                                ButtonBuilder.create().text(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Engine_Cancel")).cancelButton(true).onAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        // abort action and close the dialog.
                                        confirmationResult.set(false);
                                        dialog.close();
                                    }
                                }).build()
                        ).build()
                        , Color.TRANSPARENT
                )
        );
        configDrag(dialog);
        // style and show the dialog.
        dialog.getScene().getStylesheets().add(IOUtils.getResource("modal-dialog.css", getClass()).toExternalForm());
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                dialog.close();
            }
        });
        dialog.showAndWait();
        return confirmationResult.get();
    }

    private void configDrag(final Stage dialog) {
        // allow the dialog to be dragged around.
        final Node root = dialog.getScene().getRoot();
        final Delta dragDelta = new Delta();

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = dialog.getX() - mouseEvent.getScreenX();
                dragDelta.y = dialog.getY() - mouseEvent.getScreenY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dialog.setX(mouseEvent.getScreenX() + dragDelta.x);
                dialog.setY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
    }
}
