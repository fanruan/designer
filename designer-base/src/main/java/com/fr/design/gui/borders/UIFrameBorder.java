/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.borders;

import com.fr.design.utils.ColorRoutines;
import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-6
 * Time: 下午3:08
 */
public class UIFrameBorder extends AbstractBorder implements UIResource {

    // cache for already painted captions
    private static HashMap cache = new HashMap();

    public static Color buttonUpperColor, buttonLowerColor;
    private static UIFrameBorder onlyInstance;
    private static Robot robot;
    private static boolean robotsSupported = true;
    private Window window;
    private int titleHeight;
    private boolean isActive;

    public static UIFrameBorder getInstance() {
        if (onlyInstance == null) {
            onlyInstance = new UIFrameBorder();

            if (robot == null && robotsSupported) {
                try {
                    robot = new Robot();
                } catch (Exception ex) {
                    robotsSupported = false;
                }
            }
        }

        return onlyInstance;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        window = SwingUtilities.getWindowAncestor(c);
        isActive = window.isActive();

        if (window instanceof JFrame) {
            titleHeight = ThemeUtils.FRAME_TITLE_HEIGHT;
        } else {
            titleHeight = ThemeUtils.FRAME_INTERNAL_TITLE_HEIGHT;
        }

        if (isActive) {
            g.setColor(ThemeUtils.FRAME_BORDER_COLOR);
        } else {
            g.setColor(ThemeUtils.FRAME_BORDER_DISABLED_COLOR);
        }

        drawXpBorder(g, x, y, w, h);


        Color col = null;
        if (isActive) {
            col = ThemeUtils.FRAME_CAPTION_COLOR;
        } else {
            col = ThemeUtils.FRAME_CAPTION_DISABLED_COLOR;
        }
        g.setColor(col);

        drawXpCaption(g, x, y, w, h, col);

    }


    private void drawXpBorder(Graphics g, int x, int y, int w, int h) {
        // left
        g.drawLine(x, y + 6, x, y + h - 1);
        g.drawLine(x + 2, y + titleHeight, x + 2, y + h - 3);
        // right
        g.drawLine(x + w - 1, y + 6, x + w - 1, y + h - 1);
        g.drawLine(x + w - 3, y + titleHeight, x + w - 3, y + h - 3);
        // bottom
        g.drawLine(x + 2, y + h - 3, x + w - 3, y + h - 3);
        g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
        if (robot != null) {
            int wx = window.getLocationOnScreen().x - 4;
            int wy = window.getLocationOnScreen().y;
            Rectangle screenRect = new Rectangle(wx, wy, 4, 4);
            g.drawImage(robot.createScreenCapture(screenRect), x, y, null);
            wx = window.getLocationOnScreen().x + window.getWidth() + 1;
            screenRect = new Rectangle(wx, wy, 4, 4);
            g.drawImage(robot.createScreenCapture(screenRect), x + w - 4, y, null);
        } else {
            g.setColor(ThemeUtils.BACK_COLOR);
            g.fillRect(0, 0, w, 3);
        }
        if (isActive) {
            g.setColor(ThemeUtils.FRAME_CAPTION_COLOR);
        } else {
            g.setColor(ThemeUtils.FRAME_CAPTION_DISABLED_COLOR);
        }
        // left
        g.drawLine(x + 1, y + titleHeight, x + 1, y + h - 2);
        // right
        g.drawLine(x + w - 2, y + titleHeight, x + w - 2, y + h - 2);
        // bottom
        g.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
        Color c = null;
        if (isActive) {
            c = ThemeUtils.FRAME_BORDER_COLOR;
        } else {
            c = ThemeUtils.FRAME_BORDER_DISABLED_COLOR;
        }
        g.setColor(ColorRoutines.getAlphaColor(c, 82));
        g.drawLine(x, y + 3, x, y + 3);
        g.drawLine(x + w - 1, y + 3, x + w - 1, y + 3);
        g.setColor(ColorRoutines.getAlphaColor(c, 156));
        g.drawLine(x, y + 4, x, y + 4);
        g.drawLine(x + w - 1, y + 4, x + w - 1, y + 4);
        g.setColor(ColorRoutines.getAlphaColor(c, 215));
        g.drawLine(x, y + 5, x, y + 5);
        g.drawLine(x + w - 1, y + 5, x + w - 1, y + 5);
    }


    private void drawXpCaption(Graphics g, int x, int y, int w, int h, Color c) {
        if (titleHeight == ThemeUtils.FRAME_INTERNAL_TITLE_HEIGHT) {
            drawXpInternalCaption(g, x, y, w, h, c);
            return;
        }
        int spread1 = ThemeUtils.FRAME_SPREAD_DARK_DISABLED;
        int spread2 = ThemeUtils.FRAME_SPREAD_LIGHT_DISABLED;
        int y2 = y;
        Color borderColor = isActive ? ThemeUtils.FRAME_BORDER_COLOR : ThemeUtils.FRAME_BORDER_DISABLED_COLOR;
        drawFirstCaption(g, borderColor, x, y2, w);
        Color c2 = ColorRoutines.darken(c, 4 * spread1);
        drawSecondCaption(g, c2, c, spread2, x, y2, w);
        c2 = ColorRoutines.darken(c, 6 * spread1);
        drawThirdLine(g, c2, c, spread1, spread2, x, y2, w);
        CaptionKey key = new CaptionKey(isActive, titleHeight);
        Object value = cache.get(key);
        if (value != null) {
            g.drawImage((Image) value,
                    x + 6, y, x + w - 6, y + 5,
                    0, 0, 1, 5,
                    window);
            g.drawImage((Image) value,
                    x + 1, y + 5, x + w - 1, y + titleHeight,
                    0, 5, 1, titleHeight,
                    window);
            buttonUpperColor = ColorRoutines.darken(c, 4 * spread1);
            buttonLowerColor = ColorRoutines.lighten(c, 10 * spread2);
            return;
        }
        Image img = new BufferedImage(1, titleHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics imgGraphics = img.getGraphics();
        drawLines(img, imgGraphics, borderColor, spread1, spread2, c);
        if (isActive) {
            imgGraphics.setColor(ThemeUtils.FRAME_LIGHT_COLOR);
        } else {
            imgGraphics.setColor(ThemeUtils.FRAME_LIGHT_DISABLED_COLOR);
        }
        imgGraphics.drawLine(0, 28, 1, 28);
        imgGraphics.dispose();
        g.drawImage(img,
                x + 6, y, x + w - 6, y + 5,
                0, 0, 1, 5,
                window);
        g.drawImage(img,
                x + 1, y + 5, x + w - 1, y + titleHeight,
                0, 5, 1, titleHeight,
                window);
        cache.put(key, img);
    }


    private void drawThirdLine(Graphics g, Color c2, Color c, int spread1, int spread2, int x, int y2, int w) {
        g.setColor(c2);
        g.drawLine(x + 2, y2, x + 2, y2);
        g.setColor(ColorRoutines.getAlphaColor(c2, 139));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        y2++;
        // darker border
        g.setColor(c);
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.darken(c, 6 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        // blend from lightest color
        g.setColor(ColorRoutines.lighten(c, 10 * spread2));
        g.drawLine(x + 3, y2, x + 3, y2);
        g.drawLine(x + w - 4, y2, x + w - 4, y2);
        g.setColor(ColorRoutines.lighten(c, 7 * spread2));
        g.drawLine(x + 4, y2, x + 4, y2);
        g.drawLine(x + w - 5, y2, x + w - 5, y2);
        g.setColor(ColorRoutines.lighten(c, 3 * spread2));
        g.drawLine(x + 5, y2, x + 5, y2);
        g.drawLine(x + w - 6, y2, x + w - 6, y2);
        g.setColor(c);
        g.drawLine(x + 6, y2, x + 6, y2);
        g.drawLine(x + w - 7, y2, x + w - 7, y2);
        y2++;
        g.setColor(ColorRoutines.darken(c, 2 * spread1));
        g.drawLine(x + 5, y2, x + 6, y2);    // left
        g.drawLine(x + x + w - 7, y2, x + w - 6, y2);    // right
        // darker border
        g.setColor(ColorRoutines.darken(c, 6 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        // blend from lightest color
        g.setColor(ColorRoutines.lighten(c, 10 * spread2));
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.lighten(c, 5 * spread2));
        g.drawLine(x + 3, y2, x + 3, y2);
        g.drawLine(x + w - 4, y2, x + w - 4, y2);
        g.setColor(c);
        g.drawLine(x + 4, y2, x + 4, y2);
        g.drawLine(x + w - 5, y2, x + w - 5, y2);
        y2++;
        g.setColor(ColorRoutines.darken(c, 4 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
    }

    private void drawSecondCaption(Graphics g, Color c2, Color c, int spread2, int x, int y2, int w) {
        g.setColor(c2);
        g.drawLine(x + 3, y2, x + 5, y2);    // left
        g.drawLine(x + w - 6, y2, x + w - 4, y2);    // right
        // blend
        g.setColor(ColorRoutines.getAlphaColor(c2, 139));
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.getAlphaColor(c2, 23));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        y2++;
        // 3
        g.setColor(ColorRoutines.lighten(c, 10 * spread2));
        g.drawLine(x + 4, y2, x + 5, y2);    // left
        g.drawLine(x + w - 6, y2, x + w - 5, y2);    // right
        // darker border
        g.setColor(c);
        g.drawLine(x + 3, y2, x + 3, y2);
        g.drawLine(x + w - 4, y2, x + w - 4, y2);
    }


    private void drawFirstCaption(Graphics g, Color borderColor, int x, int y2, int w) {
        // always paint the semi-transparent parts
// 1
        // blend
        g.setColor(ColorRoutines.getAlphaColor(borderColor, 82));
        g.drawLine(x + 3, y2, x + 3, y2);
        g.drawLine(x + w - 4, y2, x + w - 4, y2);
        g.setColor(ColorRoutines.getAlphaColor(borderColor, 156));
        g.drawLine(x + 4, y2, x + 4, y2);
        g.drawLine(x + w - 5, y2, x + w - 5, y2);
        g.setColor(ColorRoutines.getAlphaColor(borderColor, 215));
        g.drawLine(x + 5, y2, x + 5, y2);
        g.drawLine(x + w - 6, y2, x + w - 6, y2);
        y2++;
    }


    private void drawLines(Image img, Graphics imgGraphics, Color borderColor, int spread1, int spread2, Color c) {
        imgGraphics.setColor(borderColor);
        imgGraphics.drawLine(0, 0, 1, 0);
        imgGraphics.setColor(ColorRoutines.darken(c, 4 * spread1));
        imgGraphics.drawLine(0, 1, 1, 1);
        imgGraphics.setColor(ColorRoutines.lighten(c, 10 * spread2));
        imgGraphics.drawLine(0, 2, 1, 2);
        imgGraphics.setColor(c);
        imgGraphics.drawLine(0, 3, 1, 3);
        imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
        imgGraphics.drawLine(0, 4, 1, 4);
        buttonUpperColor = ColorRoutines.darken(c, 4 * spread1);
        imgGraphics.setColor(buttonUpperColor);
        imgGraphics.drawLine(0, 5, 1, 5);
        imgGraphics.setColor(ColorRoutines.darken(c, 4 * spread1));
        imgGraphics.drawLine(0, 6, 1, 6);
        imgGraphics.drawLine(0, 7, 1, 7);
        imgGraphics.setColor(ColorRoutines.darken(c, 3 * spread1));
        imgGraphics.drawLine(0, 8, 1, 8);
        imgGraphics.drawLine(0, 9, 1, 9);
        imgGraphics.drawLine(0, 10, 1, 10);
        imgGraphics.drawLine(0, 11, 1, 11);
        imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
        imgGraphics.drawLine(0, 12, 1, 12);
        imgGraphics.drawLine(0, 13, 1, 13);
        imgGraphics.drawLine(0, 14, 1, 14);
        imgGraphics.setColor(ColorRoutines.darken(c, spread1));
        imgGraphics.drawLine(0, 15, 1, 15);
        imgGraphics.drawLine(0, 16, 1, 16);
        imgGraphics.setColor(c);
        imgGraphics.drawLine(0, 17, 1, 17);
        imgGraphics.drawLine(0, 18, 1, 18);
        imgGraphics.setColor(ColorRoutines.lighten(c, 2 * spread2));
        imgGraphics.drawLine(0, 19, 1, 19);
        imgGraphics.setColor(ColorRoutines.lighten(c, 4 * spread2));
        imgGraphics.drawLine(0, 20, 1, 20);
        imgGraphics.setColor(ColorRoutines.lighten(c, 5 * spread2));
        imgGraphics.drawLine(0, 21, 1, 21);
        imgGraphics.setColor(ColorRoutines.lighten(c, 6 * spread2));
        imgGraphics.drawLine(0, 22, 1, 22);
        imgGraphics.setColor(ColorRoutines.lighten(c, 8 * spread2));
        imgGraphics.drawLine(0, 23, 1, 23);
        imgGraphics.setColor(ColorRoutines.lighten(c, 9 * spread2));
        imgGraphics.drawLine(0, 24, 1, 24);
        imgGraphics.setColor(ColorRoutines.lighten(c, 10 * spread2));
        imgGraphics.drawLine(0, 25, 1, 25);
        imgGraphics.setColor(ColorRoutines.lighten(c, 4 * spread2));
        imgGraphics.drawLine(0, 26, 1, 26);
        imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
        imgGraphics.drawLine(0, 27, 1, 27);
    }

    private void drawXpInternalCaption(Graphics g, int x, int y, int w, int h, Color c) {
        int spread1 = ThemeUtils.FRAME_SPREAD_DARK_DISABLED;
        int spread2 = ThemeUtils.FRAME_SPREAD_LIGHT_DISABLED;
        int y2 = y;
        Color borderColor = null;
        paintCaptionLine(g, borderColor, spread1, spread2, x, y2, w);
        Color c2 = ColorRoutines.darken(c, 4 * spread1);
        paintFirstLine(g, c2, c, spread2, x, y2, w);
        c2 = ColorRoutines.darken(c, 6 * spread1);
        paintSecondLine(g, c2, c, spread1, spread2, x, y2, w);
        // now either paint from cache or create cached image
        CaptionKey key = new CaptionKey(isActive, titleHeight);
        Object value = cache.get(key);
        if (value != null) {
            // image is cached - paint and return
            g.drawImage((Image) value,
                    x + 6, y, x + w - 6, y + 5,
                    0, 0, 1, 5,
                    window);
            g.drawImage((Image) value,
                    x + 1, y + 5, x + w - 1, y + titleHeight,
                    0, 5, 1, titleHeight,
                    window);
            buttonUpperColor = ColorRoutines.darken(c, 4 * spread1);
            buttonLowerColor = ColorRoutines.lighten(c, 10 * spread2);

            return;
        }
        Image img = new BufferedImage(1, titleHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics imgGraphics = img.getGraphics();
        paintCaptionImage(imgGraphics, borderColor, spread1, spread2, c);
        if (isActive) {
            imgGraphics.setColor(ThemeUtils.FRAME_LIGHT_COLOR);
        } else {
            imgGraphics.setColor(ThemeUtils.FRAME_LIGHT_DISABLED_COLOR);
        }
        imgGraphics.drawLine(0, 24, 1, 24);
        imgGraphics.dispose();
        g.drawImage(img,
                x + 6, y, x + w - 6, y + 5,
                0, 0, 1, 5,
                window);
        g.drawImage(img,
                x + 1, y + 5, x + w - 1, y + titleHeight,
                0, 5, 1, titleHeight,
                window);
        cache.put(key, img);
    }

    private void paintCaptionImage(Graphics imgGraphics, Color borderColor, int spread1, int spread2, Color c) {
        imgGraphics.setColor(borderColor);
        imgGraphics.drawLine(0, 0, 1, 0);
        imgGraphics.setColor(ColorRoutines.darken(c, 4 * spread1));
        imgGraphics.drawLine(0, 1, 1, 1);
        imgGraphics.setColor(ColorRoutines.lighten(c, 10 * spread2));
        imgGraphics.drawLine(0, 2, 1, 2);
        imgGraphics.setColor(c);
        imgGraphics.drawLine(0, 3, 1, 3);
        imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
        imgGraphics.drawLine(0, 4, 1, 4);
        imgGraphics.setColor(ColorRoutines.darken(c, 4 * spread1));
        imgGraphics.drawLine(0, 5, 1, 5);
        imgGraphics.setColor(ColorRoutines.darken(c, 4 * spread1));
        imgGraphics.drawLine(0, 6, 1, 6);
        imgGraphics.setColor(ColorRoutines.darken(c, 3 * spread1));
        imgGraphics.drawLine(0, 7, 1, 7);
        imgGraphics.drawLine(0, 8, 1, 8);
        imgGraphics.drawLine(0, 9, 1, 9);
        imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
        imgGraphics.drawLine(0, 10, 1, 10);
        imgGraphics.drawLine(0, 11, 1, 11);
        imgGraphics.setColor(ColorRoutines.darken(c, spread1));
        imgGraphics.drawLine(0, 12, 1, 12);
        imgGraphics.setColor(c);
        imgGraphics.drawLine(0, 13, 1, 13);
        imgGraphics.drawLine(0, 14, 1, 14);
        imgGraphics.setColor(ColorRoutines.lighten(c, 2 * spread2));
        imgGraphics.drawLine(0, 15, 1, 15);
        imgGraphics.setColor(ColorRoutines.lighten(c, 4 * spread2));
        imgGraphics.drawLine(0, 16, 1, 16);
        imgGraphics.setColor(ColorRoutines.lighten(c, 5 * spread2));
        imgGraphics.drawLine(0, 17, 1, 17);
        imgGraphics.setColor(ColorRoutines.lighten(c, 6 * spread2));
        imgGraphics.drawLine(0, 18, 1, 18);
        imgGraphics.setColor(ColorRoutines.lighten(c, 8 * spread2));
        imgGraphics.drawLine(0, 19, 1, 19);
        imgGraphics.setColor(ColorRoutines.lighten(c, 9 * spread2));
        imgGraphics.drawLine(0, 20, 1, 20);
        imgGraphics.setColor(ColorRoutines.lighten(c, 10 * spread2));
        imgGraphics.drawLine(0, 21, 1, 21);
        imgGraphics.setColor(ColorRoutines.lighten(c, 4 * spread2));
        imgGraphics.drawLine(0, 22, 1, 22);
        imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
        imgGraphics.drawLine(0, 23, 1, 23);
    }


    private void paintSecondLine(Graphics g, Color c2, Color c, int spread1, int spread2, int x, int y2, int w) {
        g.setColor(c2);
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        // blend
        g.setColor(ColorRoutines.getAlphaColor(c2, 139));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        y2++;
        g.setColor(c);
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.darken(c, 6 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        g.setColor(ColorRoutines.lighten(c, 10 * spread2));
        g.drawLine(x + 3, y2, x + 3, y2);
        g.drawLine(x + w - 4, y2, x + w - 4, y2);
        g.setColor(ColorRoutines.lighten(c, 7 * spread2));
        g.drawLine(x + 4, y2, x + 4, y2);
        g.drawLine(x + w - 5, y2, x + w - 5, y2);
        g.setColor(ColorRoutines.lighten(c, 3 * spread2));
        g.drawLine(x + 5, y2, x + 5, y2);
        g.drawLine(x + w - 6, y2, x + w - 6, y2);
        g.setColor(c);
        g.drawLine(x + 6, y2, x + 6, y2);
        g.drawLine(x + w - 7, y2, x + w - 7, y2);
        y2++;
        g.setColor(ColorRoutines.darken(c, 2 * spread1));
        g.drawLine(x + 5, y2, x + 6, y2);    // left
        g.drawLine(x + x + w - 7, y2, x + w - 6, y2);    // right
        g.setColor(ColorRoutines.darken(c, 6 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        g.setColor(ColorRoutines.lighten(c, 10 * spread2));
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.lighten(c, 5 * spread2));
        g.drawLine(x + 3, y2, x + 3, y2);
        g.drawLine(x + w - 4, y2, x + w - 4, y2);
        g.setColor(c);
        g.drawLine(x + 4, y2, x + 4, y2);
        g.drawLine(x + w - 5, y2, x + w - 5, y2);
        y2++;
        g.setColor(ColorRoutines.darken(c, 4 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
    }

    private void paintFirstLine(Graphics g, Color c2, Color c, int spread2, int x, int y2, int w) {
        g.setColor(c2);
        g.drawLine(x + 3, y2, x + 5, y2);    // left
        g.drawLine(x + w - 6, y2, x + w - 4, y2);    // right
        // blend
        g.setColor(ColorRoutines.getAlphaColor(c2, 139));
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.getAlphaColor(c2, 23));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        y2++;
        // 3
        g.setColor(ColorRoutines.lighten(c, 10 * spread2));
        g.drawLine(x + 4, y2, x + 5, y2);    // left
        g.drawLine(x + w - 6, y2, x + w - 5, y2);    // right
        // darker border
        g.setColor(c);
        g.drawLine(x + 3, y2, x + 3, y2);
        g.drawLine(x + w - 4, y2, x + w - 4, y2);
    }


    private void paintCaptionLine(Graphics g, Color borderColor, int spread1, int spread2, int x, int y2, int w) {

        if (isActive) {
            borderColor = ThemeUtils.FRAME_BORDER_COLOR;
            spread1 = ThemeUtils.FRAME_SPREAD_DARK;
            spread2 = ThemeUtils.FRAME_SPREAD_LIGHT;
        } else {
            borderColor = ThemeUtils.FRAME_BORDER_DISABLED_COLOR;
        }

        // always paint the semi-transparent parts
// 1
        // blend
        g.setColor(ColorRoutines.getAlphaColor(borderColor, 82));
        g.drawLine(x + 3, y2, x + 3, y2);
        g.drawLine(x + w - 4, y2, x + w - 4, y2);
        g.setColor(ColorRoutines.getAlphaColor(borderColor, 156));
        g.drawLine(x + 4, y2, x + 4, y2);
        g.drawLine(x + w - 5, y2, x + w - 5, y2);
        g.setColor(ColorRoutines.getAlphaColor(borderColor, 215));
        g.drawLine(x + 5, y2, x + 5, y2);
        g.drawLine(x + w - 6, y2, x + w - 6, y2);
        y2++;
    }


    /**
     * @see javax.swing.border.Border#getBorderInsets(Component)
     */
    public Insets getBorderInsets(Component c) {
        Window w = SwingUtilities.getWindowAncestor(c);

        if (w != null && (w instanceof Frame)) {
            Frame f = (Frame) w;

            // if the frame is maximized, the border should not be visible
            if (f.getExtendedState() == (f.getExtendedState() | Frame.MAXIMIZED_BOTH)) {
                return new Insets(0, 0, 0, 0);
            }
        }

        return new Insets(0, ThemeUtils.FRAME_BORDER_WIDTH,
                ThemeUtils.FRAME_BORDER_WIDTH, ThemeUtils.FRAME_BORDER_WIDTH);
    }

    /**
     * CaptionKey is used as key in the cache HashMap.
     * Overrides equals() and hashCode().
     */
    static class CaptionKey {

        private boolean isActive;
        private int titleHeight;

        CaptionKey(boolean isActive, int titleHeight) {
            this.isActive = isActive;
            this.titleHeight = titleHeight;
        }

        public boolean equals(Object o) {
            if (o == null){
                return false;
            }
            if (!(o instanceof CaptionKey)) {
                return false;
            }

            CaptionKey other = (CaptionKey) o;

            return isActive == other.isActive &&
                    titleHeight == other.titleHeight;
        }

        public int hashCode() {
            return (isActive ? 1 : 2) * titleHeight;
        }
    }

}
