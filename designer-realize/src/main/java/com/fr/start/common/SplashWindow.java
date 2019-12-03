package com.fr.start.common;

import com.fr.design.fun.OemProcessor;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.image4j.codec.ico.ICODecoder;
import com.fr.stable.os.OperatingSystem;
import com.fr.start.OemHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.List;

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

        initTitleIcon();

        //slash pane
        this.splash = new SplashPane();

        splash.setBackground(null);
        this.setContentPane(splash);
        this.setSize(splash.getSplashDimension());

        this.setAlwaysOnTop(false);
        this.setUndecorated(true);
        //使窗体背景透明
        if (OperatingSystem.isWindows()) {
            this.setBackground(new Color(0, 0, 0, 0));
        }

        GUICoreUtils.centerWindow(this);
    }

    /**
     * 设置任务栏图标，主要用于Windows
     */
    @SuppressWarnings("unchecked")
    private void initTitleIcon() {
        try {
            OemProcessor oemProcessor = OemHandler.findOem();
            List<BufferedImage> image = null;
            if (oemProcessor != null) {
                try {
                    image = oemProcessor.createTitleIcon();
                } catch (Throwable e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            }
            if (image == null) {
                image = ICODecoder.read(SplashWindow.class
                        .getResourceAsStream("/com/fr/base/images/oem/logo.ico"));
            }
            this.setIconImages(image);
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            this.setIconImage(IOUtils.readImage("/com/fr/base/images/oem/logo.png"));
        }
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
}
