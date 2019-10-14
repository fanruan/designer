package com.fr.design.gui.iprogressbar;

import com.fr.design.border.UIRoundedBorder;
import com.fr.stable.OperatingSystem;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * 进度条带阴影的边框
 */
public class UIProgressBorder extends UIRoundedBorder {
    private int left;
    private int right;
    private int top;
    private int bottom;
    private static final long serialVersionUID = 1L;

    public UIProgressBorder(Color color) {
        super(color);
    }

    public UIProgressBorder(Color color, int thickness) {
        super(color, thickness);
    }

    public UIProgressBorder(Color color, int thickness, int roundedCorners) {
        super(color, thickness, roundedCorners);
    }

    public UIProgressBorder(int lineStyle, Color color, int roundedCorners) {
        super(lineStyle, color, roundedCorners);
    }

    public UIProgressBorder(int lineStyle, Color color, int roundedCorners, int top, int left, int bottom, int right) {
        super(lineStyle, color, roundedCorners);
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        Insets insets = new Insets(top, left, bottom, right);
        return insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getLineColor());
        g2d.fill(new RoundRectangle2D.Double(x, y, width, height, 0, 0));
        g2d.setColor(oldColor);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if(OperatingSystem.isWindows()){//mac下自带阴影
            paintBorderShadow(g2d, 7, x, y, width, height);
        }

        g2d.setColor(oldColor);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    @SuppressWarnings("squid:S2164")
    private void paintBorderShadow(Graphics2D g2, int shadowWidth, int x, int y, int width, int height) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        shadowWidth = Math.max(shadowWidth, 2);
        int sw = shadowWidth;
        for (int i = sw; i > 0; i--) {
            float pct = (float) (sw - i) / (sw - 1);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.01f * i));
            g2.setColor(getMixedColor(Color.LIGHT_GRAY, 1.0f - pct, Color.WHITE, pct));
            g2.setStroke(new BasicStroke(i));
            g2.draw(new RoundRectangle2D.Double(x, y, width, height, getRoundedCorner(), getRoundedCorner()));
        }
    }

    @SuppressWarnings("squid:S2164")
    private static Color getMixedColor(Color c1, float pct1, Color c2, float pct2) {
        float[] clr1 = c1.getComponents(null);
        float[] clr2 = c2.getComponents(null);
        for (int i = 0; i < clr1.length; i++) {
            clr1[i] = (clr1[i] * pct1) + (clr2[i] * pct2);
        }
        return new Color(clr1[0], clr1[1], clr1[2], clr1[3]);
    }
}