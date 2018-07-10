/**
 *
 */
package com.fr.start;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.general.GeneralContext;
import com.fr.stable.Constants;
import com.fr.stable.CoreGraphHelper;

/**
 * @author neil
 * @date: 2015-3-13-上午10:20:43
 */
public class SplashPane extends JPanel {

    /**
     * 获取已经绘制完毕的启动画面
     *
     * @return 绘制完毕的启动画面
     */
    public Image getSplashImage() {
        Image image = createSplashBackground();
        return CoreGraphHelper.toBufferedImage(image);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Image image = getSplashImage();
        ImageIcon imageIcon = new ImageIcon(image);
        GraphHelper.paintImage(g2d, imageIcon.getIconWidth(), imageIcon.getIconHeight(), image, Constants.IMAGE_DEFAULT, Constants.NULL, Constants.CENTER, -1, -1);
    }

    /**
     * 设置在启动过程中, 动态改变的文本, 如 当前启动的模块信息
     *
     * @param text 指定的文本
     */
    public void setShowText(String text) {

    }

    /**
     * 创建启动画面的背景图片
     *
     * @return 背景图片
     */
    public Image createSplashBackground() {
        if (GeneralContext.isChineseEnv()) {
            return BaseUtils.readImage("/com/fr/base/images/oem/splash_chinese.png");
        }

        return BaseUtils.readImage("/com/fr/base/images/oem/splash_english.png");
    }

    /**
     * 窗口关闭后取消定时获取模块信息的timer
     */
    public void releaseTimer() {

    }

}