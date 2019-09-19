package com.fr.start.common;

import com.fr.design.ui.util.UIUtil;
import com.fr.start.SplashStrategy;


/**
 * 静态启动画面
 *
 * @author vito
 * @version 10.0
 * Created by vito on 2019年9月16日
 */
public class SplashCommon implements SplashStrategy {

    private SplashWindow splashWindow;

    @Override
    public void show() {
        UIUtil.invokeLaterIfNeeded(new Runnable() {
            @Override
            public void run() {
                splashWindow = new SplashWindow();
                splashWindow.setVisible(true);
            }
        });
    }

    @Override
    public void hide() {
        UIUtil.invokeLaterIfNeeded(new Runnable() {
            @Override
            public void run() {
                if (splashWindow != null) {
                    splashWindow.setVisible(false);
                    splashWindow.dispose();
                }
            }
        });

    }

    @Override
    public void updateModuleLog(final String text) {
        UIUtil.invokeAndWaitIfNeeded(new Runnable() {
            @Override
            public void run() {
                if (splashWindow != null) {
                    splashWindow.updateModuleLog(text);
                }
            }
        });
    }

    @Override
    public void updateThanksLog(final String text) {
        UIUtil.invokeLaterIfNeeded(new Runnable() {
            @Override
            public void run() {
                if (splashWindow != null) {
                    splashWindow.updateThanksLog(text);
                }
            }
        });
    }
}
