package com.fr.design.utils.gui;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.stable.Constants;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GUIPaintUtils {

    public static final void drawBorder(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection) {
        drawBorder(g2d, x, y, width, height, isRound, rectDirection, false);
    }

    public static final void drawBorder(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection, boolean isAuthorityEdited) {
        Color oldColor = g2d.getColor();
        Shape oldClip = g2d.getClip();
        if (BaseUtils.isAuthorityEditing() && isAuthorityEdited) {
            g2d.setColor(UIConstants.AUTHORITY_LINE_COLOR);
        } else {
            g2d.setColor(UIConstants.POP_DIALOG_BORDER);
        }
        if (isRound) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (rectDirection == Constants.RIGHT) {
                g2d.clipRect(x, y, width - 2, height);
                g2d.drawRoundRect(x, y, width - 1, height - 1, UIConstants.ARC, UIConstants.ARC);
                g2d.setClip(oldClip);
                g2d.drawLine(x + width - 2, y, x + width, y);
                g2d.drawLine(x + width, y, x + width, y + height - 1);
                g2d.drawLine(x + width, y + height - 1, x + width - 2, y + height - 1);
            } else if (rectDirection == Constants.LEFT) {
                g2d.clipRect(x + 2, y, width - 2, height);
                g2d.drawRoundRect(x, y, width - 1, height - 1, UIConstants.ARC, UIConstants.ARC);
                g2d.setClip(oldClip);
                g2d.drawLine(x, y, x + 2, y);
                g2d.drawLine(x, y, x, height - 1);
                g2d.drawLine(x, height - 1, x + 3, height - 1);
            } else {
                g2d.drawRoundRect(x, y, width - 1, height - 1, UIConstants.ARC, UIConstants.ARC);
            }
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        } else {
            g2d.drawRect(x, y, width, height);
        }
        g2d.setColor(oldColor);
    }

    /**
     * 正常状态填充
     *
     * @param g2d               图形对象
     * @param x                 x坐标
     * @param y                 y坐标
     * @param width             宽度
     * @param height            高度
     * @param isRound           是否圆角
     * @param rectDirection     矩形方向
     * @param isAuthorityEdited 是否权限编辑
     * @param isPressedPainted  是否按压画
     */
    public static final void fillNormal(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection, boolean isAuthorityEdited, boolean isPressedPainted) {
        fillNormal(g2d, x, y, width, height, isRound, rectDirection, isAuthorityEdited, isPressedPainted, UIConstants.ATTRIBUTE_NORMAL);
    }

    public static final void fillNormal(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection, boolean isAuthorityEdited, boolean isPressedPainted, Color color) {
        GradientPaint gp;
        if (BaseUtils.isAuthorityEditing() && isAuthorityEdited) {
            gp = new GradientPaint(1, 1, UIConstants.AUTHORITY_BLUE, 1, height - 1, UIConstants.AUTHORITY_DARK_BLUE);
        } else if (isPressedPainted) {
            gp = new GradientPaint(1, 1, color, 1, height - 1, color);
        } else {
            gp = new GradientPaint(1, 1, UIConstants.SHADOW_GREY, 1, height - 1, UIConstants.SHADOW_GREY);
        }

        fillPaint(g2d, x, y, width, height, isRound, rectDirection, gp, UIConstants.ARC);
    }

    /**
     * 鼠标悬停状态填充
     *
     * @param g2d               图形对象
     * @param x                 x坐标
     * @param y                 y坐标
     * @param width             宽度
     * @param height            高度
     * @param isRound           是否圆角
     * @param rectDirection     矩形方向
     * @param isAuthorityEdited 是否权限编辑
     * @param isPressedPainted  是否按压画
     */
    public static final void fillRollOver(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection, boolean isAuthorityEdited, boolean isPressedPainted) {
        fillRollOver(g2d, x, y, width, height, isRound, rectDirection, isAuthorityEdited, isPressedPainted, null);
    }

    public static final void fillRollOver(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection, boolean isAuthorityEdited, boolean isPressedPainted, Color hoverColor) {
        GradientPaint gp;
        if (hoverColor != null) {
            gp = new GradientPaint(1, 1, hoverColor, 1, height - 1, hoverColor);
        } else if (BaseUtils.isAuthorityEditing() && isAuthorityEdited) {
            gp = new GradientPaint(1, 1, UIConstants.AUTHORITY_BLUE, 1, height - 1, UIConstants.HOVER_BLUE);
        } else if (isPressedPainted) {
            gp = new GradientPaint(1, 1, UIConstants.ATTRIBUTE_HOVER, 1, height - 1, UIConstants.ATTRIBUTE_HOVER);
        } else {
            gp = new GradientPaint(1, 1, UIConstants.HOVER_BLUE, 1, height - 1, UIConstants.HOVER_BLUE);
        }
        fillPaint(g2d, x, y, width, height, isRound, rectDirection, gp, UIConstants.ARC);
    }

    /**
     * 按压状态填充
     *
     * @param g2d               图形对象
     * @param x                 x坐标
     * @param y                 y坐标
     * @param width             宽度
     * @param height            高度
     * @param isRound           是否圆角
     * @param rectDirection     矩形方向
     * @param isAuthorityEdited 是否权限编辑
     */
    public static final void fillPressed(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection, boolean isAuthorityEdited) {
        fillPressed(g2d, x, y, width, height, isRound, rectDirection, isAuthorityEdited, null);
    }

    public static final void fillPressed(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection, boolean isAuthorityEdited, Color pressedColor) {
        Color oldColor = g2d.getColor();
        if (pressedColor != null) {
            g2d.setColor(pressedColor);
        } else if (BaseUtils.isAuthorityEditing() && isAuthorityEdited) {
            g2d.setColor(UIConstants.AUTHORITY_PRESS_BLUE);
        } else {
            g2d.setColor(UIConstants.ATTRIBUTE_PRESS);
        }
        Shape oldClip = g2d.getClip();
        if (isRound) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.clip(new RoundRectangle2D.Double(x, y, width, height, UIConstants.ARC, UIConstants.ARC));
            g2d.fillRoundRect(x, y, width, height, UIConstants.ARC, UIConstants.ARC);
            g2d.setClip(oldClip);
            if (rectDirection == Constants.RIGHT) {
                g2d.fillRect(width - 2, y, x + 2, height);
            } else if (rectDirection == Constants.LEFT) {
                g2d.fillRect(x, y, x + 2, height);
            }
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        } else {
            g2d.clip(new Rectangle(x, y, width, height));
            g2d.fillRect(0, 0, width, height);
            g2d.setClip(oldClip);
        }

        g2d.setColor(oldColor);
    }

    /**
     * 自定义画笔填充
     *
     * @param g2d           图形对象
     * @param x             x坐标
     * @param y             y坐标
     * @param width         宽度
     * @param height        高度
     * @param isRound       是否圆角
     * @param rectDirection 矩形方向
     * @param paint         画笔
     * @param arc           圆角尺寸
     */
    public static final void fillPaint(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection, Paint paint, int arc) {
        Paint oldPaint = g2d.getPaint();
        g2d.setPaint(paint);
        if (isRound) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.fillRoundRect(x, y, width, height, arc, arc);
            if (rectDirection == Constants.RIGHT) {
                g2d.fillRect(x + width - 2, y, 2, height);
            } else if (rectDirection == Constants.LEFT) {
                g2d.fillRect(x, y, 2, height);
            }
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        } else {
            g2d.fillRect(x, y, width, height);
        }
        g2d.setPaint(oldPaint);
    }

    private static final Color getMixedColor(Color c1, float pct1, Color c2, float pct2) {
        float[] clr1 = c1.getComponents(null);
        float[] clr2 = c2.getComponents(null);
        for (int i = 0; i < clr1.length; i++) {
            clr1[i] = (clr1[i] * pct1) + (clr2[i] * pct2);
        }
        return new Color(clr1[0], clr1[1], clr1[2], clr1[3]);
    }

    public static final void paintBorderShadow(Graphics2D g2, int shadowWidth, Shape shape, Color outColor, Color inColor) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(outColor);
        g2.setStroke(new BasicStroke(shadowWidth));
        g2.draw(shape);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

}