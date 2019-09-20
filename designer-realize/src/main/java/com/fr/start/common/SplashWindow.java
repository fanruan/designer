package com.fr.start.common;

import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.os.OperatingSystem;
import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * 启动画面窗口
 *
 * @author vito
 * @version 10.0
 * Created by vito on 2019/10/16
 */
public class SplashWindow extends JFrame {

    private SplashPane splash;

    public SplashWindow() {
        // alex:必须设置这个属性为true,才可以用透明背景
        System.setProperty("sun.java2d.noddraw", "true");

        //slash pane
        this.splash = new SplashPane();

        splash.setBackground(null);
        this.setContentPane(splash);
        this.setSize(splash.getSplashDimension());

        this.setAlwaysOnTop(false);
        this.setUndecorated(true);
        AWTUtilities.setWindowOpaque(this, false);

        //使窗体背景透明
        if (OperatingSystem.isWindows()) {
            this.setBackground(new Color(0, 0, 0, 0));
        }

        GUICoreUtils.centerWindow(this);
    }

    /**
     * 注销窗口
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * 设置在启动过程中, 动态改变的文本, 如 当前启动的模块信息
     *
     * @param text 指定的文本
     */
    void updateModuleLog(String text) {
        splash.updateModuleLog(text);
    }

    void updateThanksLog(String text) {
        splash.updateThanksLog(text);
    }


    public static void main(String[] args) {
        SplashWindow splashWindow = new SplashWindow();
        splashWindow.setVisible(true);
    }
}
