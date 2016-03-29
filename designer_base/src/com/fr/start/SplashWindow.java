package com.fr.start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.OperatingSystem;

public class SplashWindow extends JFrame {

    private SplashPane splash = null;

    @SuppressWarnings("LeakingThisInConstructor")
    public SplashWindow(SplashPane splashPane) {
        // alex:必须设置这个属性为true,才可以用透明背景
        System.setProperty("sun.java2d.noddraw", "true");

        this.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));

        JPanel defaultPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.setContentPane(defaultPane);

        defaultPane.setBackground(new Color(0, 0, 0, 0));
        //slash pane
        this.splash = splashPane;
        splash.setBackground(null);
        Image image = splash.getSplashImage();
        ImageIcon imageIcon = new ImageIcon(image);
        if (splash != null) {
            defaultPane.add(splash, BorderLayout.CENTER);
            this.setSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
        } else {
            defaultPane.add(new UILabel("Error, please contract: support@finereport.com"), BorderLayout.CENTER);
            this.setSize(new Dimension(480, 320));
        }

        this.setAlwaysOnTop(false);
        this.setUndecorated(true);
        
        //使窗体背景透明
        if (OperatingSystem.isWindows()) {
            this.setBackground(new Color(0,0,0,0));
        }
        
        GUICoreUtils.centerWindow(this);
        this.setVisible(true);
    }

    /**
     * 注销窗口
     */
    public void dispose() {
        super.dispose();
        if(this.splash != null){
        	this.splash.releaseTimer();
        }
    }

}