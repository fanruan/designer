package com.fr.start.fx;

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

    private SplashFxWindow test;
    private static final ExecutorService SERVICE = Executors.newSingleThreadExecutor();

    @Override
    public void show() {
        SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                Application.launch(SplashFxWindow.class);
            }
        });
        test = SplashFxWindow.waitForStartUpTest();
    }

    @Override
    public void hide() {
        Platform.exit();
    }

    @Override
    public void updateModuleLog(String text) {
        test.updateModuleInfo(text);
    }

    @Override
    public void updateThanksLog(String text) {
        test.updateThanks(text);
    }
}
