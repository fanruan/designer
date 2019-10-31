package com.fr.design.utils;

import com.fr.design.utils.ColorRoutines;
import com.fr.log.FineLoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;


/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-13
 * Time: 下午4:26
 */
public class DrawRoutines {
    private static final int DISPLACEMENT = 8;
    private static final int VALUE = 0xff;

    static final int[][] CHECK_A = {
            {53, 66, 78, 99, 115, 136, 144, 156, 165, 177, 189},
            {66, 78, 99, 115, 136, 144, 156, 165, 177, 189, 202},
            {78, 99, 0, 0, 0, 0, 0, 0, 0, 202, 210},
            {99, 115, 0, 0, 0, 0, 0, 0, 0, 210, 214},
            {115, 136, 0, 0, 0, 0, 0, 0, 0, 214, 226},
            {136, 144, 0, 0, 0, 0, 0, 0, 0, 226, 230},
            {144, 156, 0, 0, 0, 0, 0, 0, 0, 230, 239},
            {156, 165, 0, 0, 0, 0, 0, 0, 0, 239, 243},
            {165, 177, 0, 0, 0, 0, 0, 0, 0, 243, 247},
            {177, 189, 202, 210, 214, 226, 230, 239, 243, 247, 251},
            {189, 202, 210, 214, 226, 230, 239, 243, 247, 251, 255}
    };

    static final int[][] RADIO_A = {
            {0, 0, 78, 99, 115, 136, 144, 156, 165, 0, 0},
            {0, 78, 99, 115, 136, 144, 156, 165, 177, 189, 0},
            {78, 99, 115, 136, 92, 48, 92, 177, 189, 202, 210},
            {99, 115, 136, 0, 0, 0, 0, 0, 202, 210, 214},
            {115, 136, 92, 0, 0, 0, 0, 0, 128, 214, 226},
            {136, 144, 48, 0, 0, 0, 0, 0, 64, 226, 230},
            {144, 156, 92, 0, 0, 0, 0, 0, 128, 230, 239},
            {156, 165, 177, 0, 0, 0, 0, 0, 230, 239, 243},
            {165, 177, 189, 202, 128, 64, 128, 230, 239, 243, 247},
            {0, 189, 202, 210, 214, 226, 230, 239, 243, 247, 0},
            {0, 0, 210, 214, 226, 230, 239, 243, 247, 0, 0}
    };

    static GraphicsConfiguration conf;

    static {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        conf = ge.getDefaultScreenDevice().getDefaultConfiguration();
    }

    public static void drawBorder(Graphics g, Color c, int x, int y, int w, int h) {
        g.setColor(c);
        g.drawRect(x, y, w - 1, h - 1);
    }

    public static void drawRolloverCheckBorder(Graphics g, Color c, int x, int y, int w, int h) {
        g.translate(x, y);
        Color color;

        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                if (CHECK_A[row][col] > 0) {
                    color = new Color(c.getRed(), c.getGreen(), c.getBlue(), CHECK_A[row][col]);
                    g.setColor(color);
                    g.drawLine(col + 1, row + 1, col + 1, row + 1);
                }
            }
        }

        g.translate(-x, -y);
    }

    public static void drawXpRadioRolloverBorder(Graphics g, Color c,
                                                 int x, int y, int w, int h) {
        g.translate(x, y);
        Color color;

        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                if (RADIO_A[row][col] > 0) {
                    color = new Color(c.getRed(), c.getGreen(), c.getBlue(), RADIO_A[row][col]);
                    g.setColor(color);
                    g.drawLine(col + 1, row + 1, col + 1, row + 1);
                }
            }
        }

        g.translate(-x, -y);
    }

    public static void drawXpRadioBorder(Graphics g, Color c, int x, int y, int w, int h) {
        g.setColor(c);
        g.drawLine(x + 6, y, x + 6, y);
        g.drawLine(x + 3, y + 1, x + 3, y + 1);
        g.drawLine(x + 9, y + 1, x + 9, y + 1);
        g.drawLine(x + 1, y + 3, x + 1, y + 3);
        g.drawLine(x + 11, y + 3, x + 11, y + 3);
        g.drawLine(x, y + 6, x, y + 6);
        g.drawLine(x + 12, y + 6, x + 12, y + 6);
        g.drawLine(x + 1, y + 9, x + 1, y + 9);
        g.drawLine(x + 11, y + 9, x + 11, y + 9);
        g.drawLine(x + 3, y + 11, x + 3, y + 11);
        g.drawLine(x + 9, y + 11, x + 9, y + 11);
        g.drawLine(x + 6, y + 12, x + 6, y + 12);
        // changed alpha value from 193 to 168 in 1.3.05
        g.setColor(ColorRoutines.getAlphaColor(c, 168));
        g.drawLine(x + 5, y, x + 5, y);
        g.drawLine(x + 7, y, x + 7, y);
        g.drawLine(x + 4, y + 1, x + 4, y + 1);
        g.drawLine(x + 8, y + 1, x + 8, y + 1);
        g.drawLine(x + 2, y + 2, x + 2, y + 2);
        g.drawLine(x + 10, y + 2, x + 10, y + 2);
        g.drawLine(x + 1, y + 4, x + 1, y + 4);
        g.drawLine(x + 11, y + 4, x + 11, y + 4);
        g.drawLine(x, y + 5, x, y + 5);
        g.drawLine(x + 12, y + 5, x + 12, y + 5);
        g.drawLine(x, y + 7, x, y + 7);
        g.drawLine(x + 12, y + 7, x + 12, y + 7);
        g.drawLine(x + 1, y + 8, x + 1, y + 8);
        g.drawLine(x + 11, y + 8, x + 11, y + 8);
        g.drawLine(x + 2, y + 10, x + 2, y + 10);
        g.drawLine(x + 10, y + 10, x + 10, y + 10);
        g.drawLine(x + 4, y + 11, x + 4, y + 11);
        g.drawLine(x + 8, y + 11, x + 8, y + 11);
        g.drawLine(x + 5, y + 12, x + 5, y + 12);
        g.drawLine(x + 7, y + 12, x + 7, y + 12);
        drawOther(g, c, x, y, w, h);
        // changed alpha value from 43 to 16 in 1.3.05
        g.setColor(ColorRoutines.getAlphaColor(c, 16));
        g.drawLine(x + 3, y, x + 3, y);
        g.drawLine(x + 9, y, x + 9, y);
        g.drawLine(x, y + 3, x, y + 3);
        g.drawLine(x + 12, y + 3, x + 12, y + 3);
        g.drawLine(x, y + 9, x, y + 9);
        g.drawLine(x + 12, y + 9, x + 12, y + 9);
        g.drawLine(x + 3, y + 12, x + 3, y + 12);
        g.drawLine(x + 9, y + 12, x + 9, y + 12);
    }

    private static void drawOther(Graphics g, Color c, int x, int y, int w, int h) {
        g.setColor(ColorRoutines.getAlphaColor(c, 64));
        g.drawLine(x + 4, y, x + 4, y);
        g.drawLine(x + 8, y, x + 8, y);
        g.drawLine(x + 2, y + 1, x + 2, y + 1);
        g.drawLine(x + 2, y + 3, x + 2, y + 3);
        g.drawLine(x + 10, y + 1, x + 10, y + 1);
        g.drawLine(x + 10, y + 3, x + 10, y + 3);
        g.drawLine(x + 5, y + 1, x + 5, y + 1);
        g.drawLine(x + 7, y + 1, x + 7, y + 1);
        g.drawLine(x + 1, y + 2, x + 1, y + 2);
        g.drawLine(x + 1, y + 5, x + 1, y + 5);
        g.drawLine(x + 1, y + 7, x + 1, y + 7);
        g.drawLine(x + 11, y + 2, x + 11, y + 2);
        g.drawLine(x + 3, y + 2, x + 3, y + 2);
        g.drawLine(x + 9, y + 2, x + 9, y + 2);
        g.drawLine(x, y + 4, x, y + 4);
        g.drawLine(x + 12, y + 4, x + 12, y + 4);
        g.drawLine(x, y + 8, x, y + 8);
        g.drawLine(x + 12, y + 8, x + 12, y + 8);
        g.drawLine(x + 2, y + 9, x + 2, y + 9);
        g.drawLine(x + 10, y + 9, x + 10, y + 9);
        g.drawLine(x + 1, y + 10, x + 1, y + 10);
        g.drawLine(x + 11, y + 5, x + 11, y + 5);
        g.drawLine(x + 11, y + 7, x + 11, y + 7);
        g.drawLine(x + 11, y + 10, x + 11, y + 10);
        g.drawLine(x + 3, y + 10, x + 3, y + 10);
        g.drawLine(x + 9, y + 10, x + 9, y + 10);
        g.drawLine(x + 2, y + 11, x + 2, y + 11);
        g.drawLine(x + 10, y + 11, x + 10, y + 11);
        g.drawLine(x + 5, y + 11, x + 5, y + 11);
        g.drawLine(x + 7, y + 11, x + 7, y + 11);
        g.drawLine(x + 4, y + 12, x + 4, y + 12);
        g.drawLine(x + 8, y + 12, x + 8, y + 12);
    }

    /**
     * 颜色画法规则
     *
     * @param img          图片
     * @param hue          画法
     * @param sat          值sat
     * @param bri          亮度
     * @param preserveGrey 灰度
     * @return 处理过的图片
     */
    public static ImageIcon colorize(Image img, int hue, int sat, int bri, boolean preserveGrey) {
        ColorRoutines nc = new ColorRoutines(hue, sat, bri, preserveGrey);

        int w = img.getWidth(null);
        int h = img.getHeight(null);

        BufferedImage bufferedImg = conf.createCompatibleImage(w, h, Transparency.TRANSLUCENT);

        int[] pixels = new int[w * h];
        PixelGrabber grabber = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);

        try {
            grabber.grabPixels();
        } catch (InterruptedException e) {
            FineLoggerFactory.getLogger().error("PixelGrabber interrupted waiting for pixels");
            Thread.currentThread().interrupt();
        }

        if ((grabber.getStatus() & ImageObserver.ABORT) != 0) {
            FineLoggerFactory.getLogger().info("Image fetch aborted or errored.");
        } else {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    bufferedImg.setRGB(x, y, colorize(pixels[y * w + x], nc));
                }
            }
        }

        return new ImageIcon(bufferedImg);
    }

    protected static int colorize(int px, ColorRoutines nc) {
        int a = (px >> (DISPLACEMENT * 3)) & VALUE;
        if (a == 0) {
            return px;
        }
        int r = (px >> (DISPLACEMENT * 2)) & VALUE;
        int g = (px >> DISPLACEMENT) & VALUE;
        int b = px & VALUE;

        return nc.colorize(r, g, b, a);
    }

    public static void drawEditableComboBorder(
            Graphics g, Color c, int x, int y, int w, int h) {
        // changed this in 1.3 so the border paints like a
        // rounded border without a right side
        g.setColor(c);
        // rect - no right side
        g.drawLine(x, y + 3, x, h - 4);            // left
        g.drawLine(x + 3, y, w - 1, y);            // top
        g.drawLine(x + 3, h - 1, w - 1, h - 1);    // bottom
        // edges verl鋘gerungen 1
        Color c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 216);
        g.setColor(c2);
        // oben
        g.drawLine(x + 2, y, x + 2, y);
        g.drawLine(x + w - 3, y, x + w - 3, y);
        // links
        g.drawLine(x, y + 2, x, y + 2);
        g.drawLine(x, y + h - 3, x, y + h - 3);
        // unten
        g.drawLine(x + 2, y + h - 1, x + 2, y + h - 1);
        g.drawLine(x + w - 3, y + h - 1, x + w - 3, y + h - 1);
        // edges verl鋘gerungen 2
        c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 130);
        g.setColor(c2);
        // oben
        g.drawLine(x + 1, y, x + 1, y);
        // links
        g.drawLine(x, y + 1, x, y + 1);
        g.drawLine(x, y + h - 2, x, y + h - 2);
        // unten
        g.drawLine(x + 1, y + h - 1, x + 1, y + h - 1);
        // edges aussen
        c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 24);
        g.setColor(c2);
        // lo
        g.drawLine(x, y, x, y);
        // lu
        g.drawLine(x, y + h - 1, x, y + h - 1);
        // edges innen
        c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 112);
        g.setColor(c2);
        // lo
        g.drawLine(x + 1, y + 1, x + 1, y + 1);

        c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 104);
        g.setColor(c2);
        // lu
        g.drawLine(x + 1, y + h - 2, x + 1, y + h - 2);
    }

    public static void drawRoundedBorder(Graphics g, Color c, int x, int y, int w, int h) {
        g.setColor(c);
        // rect
        g.drawLine(x + 3, y, x + w - 4, y);                    // top
        g.drawLine(x + 3, y + h - 1, x + w - 4, y + h - 1);    // bottom
        g.drawLine(x, y + 3, x, y + h - 4);                    // left
        g.drawLine(x + w - 1, y + 3, x + w - 1, y + h - 4);    // right
        // edges verl鋘gerungen 1
        Color c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 216);
        g.setColor(c2);
        // oben
        g.drawLine(x + 2, y, x + 2, y);
        g.drawLine(x + w - 3, y, x + w - 3, y);
        // links
        g.drawLine(x, y + 2, x, y + 2);
        g.drawLine(x, y + h - 3, x, y + h - 3);
        // unten
        g.drawLine(x + 2, y + h - 1, x + 2, y + h - 1);
        g.drawLine(x + w - 3, y + h - 1, x + w - 3, y + h - 1);
        // rechts
        g.drawLine(x + w - 1, y + 2, x + w - 1, y + 2);
        g.drawLine(x + w - 1, y + h - 3, x + w - 1, y + h - 3);
        // edges verl鋘gerungen 2
        c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 130);
        g.setColor(c2);
        g.drawLine(x + 1, y, x + 1, y);
        g.drawLine(x + w - 2, y, x + w - 2, y);
        g.drawLine(x, y + 1, x, y + 1);
        g.drawLine(x, y + h - 2, x, y + h - 2);
        g.drawLine(x + 1, y + h - 1, x + 1, y + h - 1);
        g.drawLine(x + w - 2, y + h - 1, x + w - 2, y + h - 1);
        g.drawLine(x + w - 1, y + 1, x + w - 1, y + 1);
        g.drawLine(x + w - 1, y + h - 2, x + w - 1, y + h - 2);
        c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 24);
        g.setColor(c2);
        g.drawLine(x, y, x, y);
        g.drawLine(x + w - 1, y, x + w - 1, y);
        g.drawLine(x, y + h - 1, x, y + h - 1);
        g.drawLine(x + w - 1, y + h - 1, x + w - 1, y + h - 1);
        c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 112);
        g.setColor(c2);
        g.drawLine(x + 1, y + 1, x + 1, y + 1);
        g.drawLine(x + w - 2, y + 1, x + w - 2, y + 1);
        c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 104);
        g.setColor(c2);
        g.drawLine(x + 1, y + h - 2, x + 1, y + h - 2);
        g.drawLine(x + w - 2, y + h - 2, x + w - 2, y + h - 2);
    }

    public static synchronized void drawProgressBarBorder(
            Graphics g, Color c, int x, int y, int w, int h) {
        g.setColor(c);
        // rect
        g.drawLine(x + 1, y, x + w - 2, y);
        g.drawLine(x + 1, y + h - 1, x + w - 2, y + h - 1);
        g.drawLine(x, y + 1, x, y + h - 2);
        g.drawLine(x + w - 1, y + 1, x + w - 1, y + h - 2);

        // edges innen
        // lo
        g.drawLine(x + 1, y + 1, x + 1, y + 1);
        // ro
        g.drawLine(x + w - 2, y + 1, x + w - 2, y + 1);
        // lu
        g.drawLine(x + 1, y + h - 2, x + 1, y + h - 2);
        // ru
        g.drawLine(x + w - 2, y + h - 2, x + w - 2, y + h - 2);
    }
}