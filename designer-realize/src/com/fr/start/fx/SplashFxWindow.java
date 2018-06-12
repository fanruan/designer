package com.fr.start.fx;

import com.bulenkov.iconloader.util.JBUI;
import com.fr.base.FRContext;
import com.fr.stable.OperatingSystem;
import com.fr.start.SplashContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.CountDownLatch;


/**
 * JavaFx启动动画窗口
 *
 * @author vito
 */
public class SplashFxWindow extends Application {

    private static float JBUI_INIT_SCALE = JBUI.scale(1f);

    private static final String ARIAL_FONT_NAME = "Arial";
    private static final String PF_FONT_NAME = "PingFang";
    private static final String YAHEI_FONT_NAME = "Microsoft YaHei";
    private static final int MODULE_INFO_LEFT_MARGIN = 36;
    private static final int MODULE_INFO_BOTTOM_MARGIN = 28;
    private static final int THINKS_BOTTOM_RIGHT = 35;
    private static final int THINKS_BOTTOM_MARGIN = 27;
    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 360;
    private static final int FONT = 12;
    private static final int FRAME_COUNT = 315;
    private static final String THINKS_COLOR = "#82b1ce";

    private static final CountDownLatch LATCH = new CountDownLatch(1);
    private static SplashFxWindow app = null;

    private Text moduleInfo;
    private Text thanks;

    private static int uiScale(int i) {
        return (int) (i * JBUI_INIT_SCALE);
    }

    /**
     * 获取当前运行实例。黑科技
     *
     * @return 运行实例
     */
    public static SplashFxWindow waitForStartUpTest() {
        try {
            LATCH.await();
        } catch (InterruptedException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return app;
    }

    private static void setApp(SplashFxWindow window) {
        app = window;
        LATCH.countDown();
    }

    public SplashFxWindow() {
        setApp(this);
    }

    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Image image = new FastGifImage(SplashContext.SPLASH_PATH, FRAME_COUNT, WINDOW_WIDTH, WINDOW_HEIGHT);

        ImageView gif = new ImageView(image);

        AnchorPane.setBottomAnchor(gif, 0d);
        AnchorPane.setTopAnchor(gif, 0d);
        AnchorPane.setLeftAnchor(gif, 0d);
        AnchorPane.setRightAnchor(gif, 0d);
        Font font;
        if (OperatingSystem.isWindows()) {
            font = new Font(YAHEI_FONT_NAME, uiScale(FONT));
        } else if (OperatingSystem.isMacOS()) {
            font = new Font(PF_FONT_NAME, uiScale(FONT));
        } else {
            font = new Font(ARIAL_FONT_NAME, uiScale(FONT));
        }

        moduleInfo = new Text();
        moduleInfo.setFont(font);
        moduleInfo.setFill(Color.WHITE);
        AnchorPane.setLeftAnchor(moduleInfo, (double) uiScale(MODULE_INFO_LEFT_MARGIN));
        AnchorPane.setBottomAnchor(moduleInfo, (double) uiScale(MODULE_INFO_BOTTOM_MARGIN));
        thanks = new Text();
        thanks.setFont(font);
        thanks.setFill(Color.valueOf(THINKS_COLOR));
        AnchorPane.setRightAnchor(thanks, (double) uiScale(THINKS_BOTTOM_RIGHT));
        AnchorPane.setBottomAnchor(thanks, (double) uiScale(THINKS_BOTTOM_MARGIN));

        root.getChildren().add(gif);
        root.getChildren().add(moduleInfo);
        root.getChildren().add(thanks);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void close() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ((Stage) moduleInfo.getScene().getWindow()).close();
                } catch (Exception e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
            }
        });
    }

    /**
     * 更新模块信息
     *
     * @param s 文字
     */
    public void updateModuleInfo(final String s) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (moduleInfo != null) {
                    moduleInfo.setText(s);
                }
            }
        });

    }

    /**
     * 更新欢迎信息
     *
     * @param s 文字
     */
    public void updateThanks(final String s) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (thanks != null) {
                    thanks.setText(s);
                }
            }
        });

    }
}
