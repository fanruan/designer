package com.fr.start.fx;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.start.SplashFxActionListener;
import com.fr.start.SplashStrategy;
import javafx.application.Application;
import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * JavaFx方式启动启动动画。这种方式在mac下与
 * swing一起启动会会出现线程死锁，jvm等问题，
 * 所以这个方式仅用于windows上。
 *
 * @author vito
 * @date 2018/6/4
 * @see com.fr.start.jni.SplashMac
 */
public class SplashFx implements SplashStrategy {

    private SplashFxWindow fxWindow;
    private static final ExecutorService SERVICE = Executors.newSingleThreadExecutor(new NamedThreadFactory("SplashFx"));

    @Override
    public void show() {
        Platform.setImplicitExit(false);
        SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                Application.launch(SplashFxWindow.class);
            }
        });
        fxWindow = SplashFxWindow.waitForStartUpTest();
        fxWindow.addSplashActionListener(new SplashFxActionListener() {
            @Override
            public void splashClose() {
                DesignerContext.getDesignerFrame().setVisible(true);
            }
        });
    }

    @Override
    public void hide() {
        fxWindow.close();
    }

    @Override
    public void updateModuleLog(final String text) {
        fxWindow.updateModuleInfo(text);
    }

    @Override
    public void updateThanksLog(final String text) {
        fxWindow.updateThanks(text);
    }
}
