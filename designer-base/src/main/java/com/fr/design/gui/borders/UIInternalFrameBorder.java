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
 * Date: 14-3-5
 * Time: 下午3:13
 */
public class UIInternalFrameBorder extends AbstractBorder implements UIResource {
    private static HashMap cache = new HashMap();

    public static Color frameUpperColor, frameLowerColor;
    public static Color disabledUpperColor, disabledLowerColor;
    private JInternalFrame frame;
    private boolean isPalette;
    private int titleHeight;

    /**
     * indicates whether the internal frame is active
     */
    private boolean isActive;

    /**
     * @see javax.swing.border.Border#paintBorder(Component, Graphics, int, int, int, int)
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        frame = (JInternalFrame) c;

        if (ThemeUtils.FRAME_IS_TRANSPARENT) {
            frame.setOpaque(false);
        }

        isActive = frame.isSelected();
        isPalette = (frame.getClientProperty("isPalette") == Boolean.TRUE);

        if (isPalette) {
            titleHeight = ThemeUtils.FRAME_PALETTE_TITLE_HEIGHT;
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

        // outer blend over 3 px
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
        // Note: this method is equal to TinyFrameBorder.drawXpInternalCaption()
        if (isPalette) {
            drawXpPaletteCaption(g, x, y, w, h, c);
            return;
        }
        int spread1 = ThemeUtils.FRAME_SPREAD_DARK_DISABLED;
        int spread2 = ThemeUtils.FRAME_SPREAD_LIGHT_DISABLED;
        int y2 = y;
        Color borderColor = null;
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
        drawFirstLine(g, x, y, w, h, y2, spread1, spread2, c, borderColor);
        // 4
        // darker border
        drawSecondLine(g, x, y, w, h, y2, spread1, spread2, c);
        // now either paint from cache or create cached image
        CaptionKey key = new CaptionKey(isActive, titleHeight);
        Object value = cache.get(key);
        if (value != null) {
            g.drawImage((Image) value,
                    x + 6, y, x + w - 6, y + 5,
                    0, 0, 1, 5,
                    frame);
            g.drawImage((Image) value,
                    x + 1, y + 5, x + w - 1, y + titleHeight,
                    0, 5, 1, titleHeight,
                    frame);
            if (isActive) {
                frameUpperColor = ColorRoutines.darken(c, 4 * spread1);
                frameLowerColor = ColorRoutines.lighten(c, 10 * spread2);
            } else {
                disabledUpperColor = ColorRoutines.darken(c, 4 * spread1);
                disabledLowerColor = ColorRoutines.lighten(c, 10 * spread2);
            }
            return;
        }
        Image img = new BufferedImage(1, titleHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics imgGraphics = img.getGraphics();
        drawImageLine(imgGraphics, borderColor, c, spread1, spread2);
        drawImageLast(img, imgGraphics, g, x, y, w, key);
    }

    private void drawImageLast(Image img, Graphics imgGraphics, Graphics g, int x, int y, int w, CaptionKey key) {
        // 25
        if (isActive) {
            imgGraphics.setColor(ThemeUtils.FRAME_LIGHT_COLOR);
        } else {
            imgGraphics.setColor(ThemeUtils.FRAME_LIGHT_DISABLED_COLOR);
        }
        imgGraphics.drawLine(0, 24, 1, 24);

        // dispose of image graphics
        imgGraphics.dispose();

        // paint image
        g.drawImage(img,
                x + 6, y, x + w - 6, y + 5,
                0, 0, 1, 5,
                frame);
        g.drawImage(img,
                x + 1, y + 5, x + w - 1, y + titleHeight,
                0, 5, 1, titleHeight,
                frame);

        // add the image to the cache
        cache.put(key, img);
    }

    private void drawImageLine(Graphics imgGraphics, Color borderColor, Color c, int spread1, int spread2) {
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

    private void drawFirstLine(Graphics g, int x, int y, int w, int h, int y2, int spread1, int spread2, Color c, Color borderColor) {
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
        // 2
        Color c2 = ColorRoutines.darken(c, 4 * spread1);
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
        c2 = ColorRoutines.darken(c, 6 * spread1);
        g.setColor(c2);
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        // blend
        g.setColor(ColorRoutines.getAlphaColor(c2, 139));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        y2++;
    }

    private void drawSecondLine(Graphics g, int x, int y, int w, int h, int y2, int spread1, int spread2, Color c) {
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
        // 6
        // lighten little
        g.setColor(ColorRoutines.darken(c, 4 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
    }

    private void drawXpPaletteCaption(Graphics g, int x, int y, int w, int h, Color c) {
        int y2 = y;
        int spread1 = ThemeUtils.FRAME_SPREAD_DARK_DISABLED;
        int spread2 = ThemeUtils.FRAME_SPREAD_LIGHT_DISABLED;
        Color borderColor = null;
        if (isActive) {
            borderColor = ThemeUtils.FRAME_BORDER_COLOR;
            spread1 = ThemeUtils.FRAME_SPREAD_DARK;
            spread2 = ThemeUtils.FRAME_SPREAD_LIGHT;
        } else {
            borderColor = ThemeUtils.FRAME_BORDER_DISABLED_COLOR;
        }
        // always paint the semi-transparent parts
        paintFirstLine(g, x, y, w, h, y2, spread1, spread2, c, borderColor);
        // blend from lightest color
        paintSecondLine(g, x, y, w, h, y2, spread1, spread2, c);
        // now either paint from cache or create cached image
        CaptionKey key = new CaptionKey(isActive, titleHeight);
        Object value = cache.get(key);

        if (value != null) {
            // image is cached - paint and return
            g.drawImage((Image) value,
                    x + 6, y, x + w - 6, y + 5,
                    0, 0, 1, 5,
                    frame);
            g.drawImage((Image) value,
                    x + 1, y + 5, x + w - 1, y + titleHeight,
                    0, 5, 1, titleHeight,
                    frame);

            // store button colors
            if (isActive) {
                frameUpperColor = ColorRoutines.darken(c, 4 * spread1);
                frameLowerColor = ColorRoutines.lighten(c, 10 * spread2);
            } else {
                disabledUpperColor = ColorRoutines.darken(c, 4 * spread1);
                disabledLowerColor = ColorRoutines.lighten(c, 10 * spread2);
            }

            return;
        }

        Image img = new BufferedImage(1, titleHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics imgGraphics = img.getGraphics();
        paintImageLine(imgGraphics, borderColor, c, spread1, spread2);
        paintImageLast(img, imgGraphics, g, x, y, w, key);
    }


    private void paintImageLast(Image img, Graphics imgGraphics, Graphics g, int x, int y, int w, CaptionKey key) {
        // 21
        if (isActive) {
            imgGraphics.setColor(ThemeUtils.FRAME_LIGHT_COLOR);
        } else {
            imgGraphics.setColor(ThemeUtils.FRAME_LIGHT_DISABLED_COLOR);
        }
        imgGraphics.drawLine(0, 20, 1, 20);

        // dispose of image graphics
        imgGraphics.dispose();

        // paint image
        g.drawImage(img,
                x + 6, y, x + w - 6, y + 5,
                0, 0, 1, 5,
                frame);
        g.drawImage(img,
                x + 1, y + 5, x + w - 1, y + titleHeight,
                0, 5, 1, titleHeight,
                frame);

        // add the image to the cache
        cache.put(key, img);

    }

    private void paintImageLine(Graphics imgGraphics, Color borderColor, Color c, int spread1, int spread2) {
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
        imgGraphics.drawLine(0, 6, 1, 6);
        imgGraphics.setColor(ColorRoutines.darken(c, 3 * spread1));
        imgGraphics.drawLine(0, 7, 1, 7);
        imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
        imgGraphics.drawLine(0, 8, 1, 8);
        imgGraphics.setColor(ColorRoutines.darken(c, spread1));
        imgGraphics.drawLine(0, 9, 1, 9);
        imgGraphics.setColor(c);
        imgGraphics.drawLine(0, 10, 1, 10);
        imgGraphics.setColor(ColorRoutines.lighten(c, 2 * spread2));
        imgGraphics.drawLine(0, 11, 1, 11);
        imgGraphics.setColor(ColorRoutines.lighten(c, 4 * spread2));
        imgGraphics.drawLine(0, 12, 1, 12);
        imgGraphics.setColor(ColorRoutines.lighten(c, 5 * spread2));
        imgGraphics.drawLine(0, 13, 1, 13);
        imgGraphics.setColor(ColorRoutines.lighten(c, 6 * spread2));
        imgGraphics.drawLine(0, 14, 1, 14);
        imgGraphics.setColor(ColorRoutines.lighten(c, 8 * spread2));
        imgGraphics.drawLine(0, 15, 1, 15);
        imgGraphics.setColor(ColorRoutines.lighten(c, 9 * spread2));
        imgGraphics.drawLine(0, 16, 1, 16);
        imgGraphics.setColor(ColorRoutines.lighten(c, 10 * spread2));
        imgGraphics.drawLine(0, 17, 1, 17);
        imgGraphics.setColor(ColorRoutines.lighten(c, 4 * spread2));
        imgGraphics.drawLine(0, 18, 1, 18);
        imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
        imgGraphics.drawLine(0, 19, 1, 19);
    }

    private void paintSecondLine(Graphics g, int x, int y, int w, int h, int y2, int spread1, int spread2, Color c) {
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
        // 5
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
        // 6
        // lighten little
        g.setColor(ColorRoutines.darken(c, 4 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);/**/
    }

    private void paintFirstLine(Graphics g, int x, int y, int w, int h, int y2, int spread1, int spread2, Color c, Color borderColor) {
        // 1
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
        // 2
        Color c2 = ColorRoutines.darken(c, 4 * spread1);
        g.setColor(c2);
        g.drawLine(x + 3, y2, x + 5, y2);    // left
        g.drawLine(x + w - 6, y2, x + w - 4, y2);    // right
        g.setColor(ColorRoutines.getAlphaColor(c2, 139));
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.getAlphaColor(c2, 23));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        y2++;
        g.setColor(ColorRoutines.lighten(c, 10 * spread2));
        g.drawLine(x + 4, y2, x + 5, y2);    // left
        g.drawLine(x + w - 6, y2, x + w - 5, y2);    // right
        // darker border
        g.setColor(c);
        g.drawLine(x + 3, y2, x + 3, y2);
        g.drawLine(x + w - 4, y2, x + w - 4, y2);
        c2 = ColorRoutines.darken(c, 6 * spread1);
        g.setColor(c2);
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.getAlphaColor(c2, 139));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        y2++;
        // 4
        // darker border
        g.setColor(c);
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.darken(c, 6 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
    }


    /**
     * @see javax.swing.border.Border#getBorderInsets(Component)
     */
    public Insets getBorderInsets(Component c) {
        JInternalFrame frame = (JInternalFrame) c;

        // if the frame is maximized, the border should not be visible
        if (frame.isMaximum()) {
            return new Insets(0, 0, 0, 0);
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
            if (!(o instanceof CaptionKey)){
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