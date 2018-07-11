/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.ispinner;

import com.fr.design.gui.ibutton.UIBasicButtonUI;
import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-5
 * Time: 下午4:05
 */
public class UISpinnerButtonUI extends UIBasicButtonUI {
    private int orientation;

    protected static Dimension xpSize = new Dimension(15, 8);

    /**
     * 创建UI
     * @param c 组件
     * @return 组件UI
     */
    public static ComponentUI createUI(JComponent c) {
        throw new IllegalStateException("Must not be used this way.");
    }

    /**
     * Creates a new Spinner Button. Use either SwingConstants.SOUTH or SwingConstants.NORTH
     * for a SpinnerButton of Type up or a down.
     *
     * @param type
     */
    UISpinnerButtonUI(int type) {
        orientation = type;
    }

    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;

        if (!button.isEnabled()) {
            g.setColor(ThemeUtils.SPINNER_BUTT_DISABLED_COLOR);
        } else if (button.getModel().isPressed()) {
            g.setColor(ThemeUtils.SPINNER_BUTT_PRESSED_COLOR);
        } else if (button.getModel().isRollover()) {
            g.setColor(ThemeUtils.SPINNER_BUTT_ROLLOVER_COLOR);
        } else {
            g.setColor(ThemeUtils.SPINNER_BUTT_COLOR);
        }

        drawXpButton(g, button);


        if (!button.isEnabled()) {
            g.setColor(ThemeUtils.SPINNER_ARROW_DISABLED_COLOR);
        } else {
            g.setColor(ThemeUtils.SPINNER_ARROW_COLOR);
        }

        drawXpArrow(g, button);

    }

    private void drawXpButton(Graphics g, AbstractButton b) {
        //    	g.setColor(Color.RED);
        //    	g.fillRect(0, 0, b.getWidth(), b.getHeight());
        //    	if(true) return;
        Color borderColor = ThemeUtils.BORDER_COLOR;
        Color innerColor = ThemeUtils.WHITE_BORDER_COLOR;
        int h = b.getSize().height;
        int w = b.getSize().width;

        boolean isEnabled = b.isEnabled();
        boolean isRollover = b.getModel().isRollover();
        boolean isPressed = b.getModel().isPressed();
        Color pressedLightColor = ThemeUtils.PRESSED_LIGHT_COLOR;
        Color pressedDarkColor = ThemeUtils.PRESSED_DARK_COLOR;
        Color rolloverLightColor = ThemeUtils.ROLLOVER_LIGHT_COLOR;
        Color rolloverDarkColor = ThemeUtils.ROLLOVER_DARK_COLOR;
        Color normalLightColor = ThemeUtils.NORMAL_LIGHT_COLOR;
        Color normalDarkColor = ThemeUtils.NORMAL_DARK_COLOR;

        Graphics2D g2 = (Graphics2D) g;
        GradientPaint spinnerBg = new GradientPaint(0, 0, normalLightColor, 0, h, normalDarkColor);
        GradientPaint HighLight = new GradientPaint(2, 2, new Color(1.0f, 1.0f, 1.0f, 0.6f), 0, h / 2, new Color(1.0f, 1.0f, 1.0f, 0.2f));
        GradientPaint spinnerCover = new GradientPaint(0, 0, rolloverLightColor, 0, h, rolloverDarkColor);
        GradientPaint spinnerPressed = new GradientPaint(0, 0, pressedLightColor, 0, h, pressedDarkColor);
        g2.setPaint(spinnerBg);
        g2.fillRoundRect(2, 2, w - 4, h - 4, 3, 3);
        g2.setPaint(HighLight);
        g2.fillRect(2, 2, w - 4, (h - 4) / 2);
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, w - 1, h - 1, 3, 3);
        g2.setColor(innerColor);
        g2.drawRoundRect(1, 1, w - 3, h - 3, 3, 3);

        if (isRollover) {
            g2.setPaint(spinnerCover);
            g2.fillRoundRect(2, 2, w - 4, h - 4, 3, 3);
        } else if (isPressed) {
            g2.setPaint(spinnerPressed);
            g2.fillRoundRect(2, 2, w - 4, h - 4, 3, 3);
        } else if (!isEnabled) {
            g2.setColor(ThemeUtils.SPINNER_BUTT_DISABLED_COLOR);
            g2.fillRoundRect(2, 2, w - 4, h - 4, 3, 3);
        }
    }


    private void drawXpArrow(Graphics g, AbstractButton b) {
        int y = (b.getSize().height - 6) / 2;

        switch (orientation) {
            case SwingConstants.NORTH:
                y--;
                g.drawLine(7, y + 2, 7, y + 2);
                g.drawLine(6, y + 3, 8, y + 3);
                g.drawLine(5, y + 4, 9, y + 4);
                g.drawLine(4, y + 5, 6, y + 5);
                g.drawLine(8, y + 5, 10, y + 5);
                break;
            case SwingConstants.SOUTH:
                g.drawLine(4, y + 2, 6, y + 2);
                g.drawLine(8, y + 2, 10, y + 2);
                g.drawLine(5, y + 3, 9, y + 3);
                g.drawLine(6, y + 4, 8, y + 4);
                g.drawLine(7, y + 5, 7, y + 5);
                break;
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicButtonUI#getPreferredSize(javax.swing.JComponent)
     */
    public Dimension getPreferredSize(JComponent c) {
        return xpSize;
    }

}