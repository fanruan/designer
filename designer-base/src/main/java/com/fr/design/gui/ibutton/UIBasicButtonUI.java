/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.ibutton;

import com.fr.design.gui.itoolbar.UIToolBarUI;
import com.fr.design.utils.ColorRoutines;
import com.fr.design.utils.ThemeUtils;
import com.fr.general.ComparatorUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-19
 * Time: 上午9:29
 */
public class UIBasicButtonUI extends MetalButtonUI {

    // if a button has not the defined background, it will
    // be darkened resp. lightened by BG_CHANGE amount if
    // pressed or rollover
    public static final int BG_CHANGE_AMOUNT = 10;

    /**
     * The Cached UI delegate.
     */
    private static final UIBasicButtonUI BUTTON_UI = new UIBasicButtonUI();

    /* the only instance of the stroke for the focus */
    private static final BasicStroke FOCUS_STROKE =
            new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[]{1.0f, 1.0f}, 0.0f);

    private boolean graphicsTranslated;
    private boolean isToolBarButton, isFileChooserButton;
    private boolean isDefault;

    public UIBasicButtonUI() {
    }

    /**
     * 初始化UI
     *
     * @param c 组件c
     */
    public void installUI(JComponent c) {
        super.installUI(c);

        if (!ThemeUtils.BUTTON_ENTER) {
            return;
        }
        if (!c.isFocusable()) {
            return;
        }

        InputMap km = (InputMap) UIManager.get(getPropertyPrefix() + "focusInputMap");

        if (km != null) {
            // replace SPACE with ENTER (but SPACE will still work, don't know why)
            km.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "pressed");
            km.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "released");
        }
    }

    /**
     * 加载默认的
     *
     * @param button 按钮
     */
    public void installDefaults(AbstractButton button) {
        super.installDefaults(button);
        button.setRolloverEnabled(true);
    }

    protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
        boolean isToolButtonFocus = isToolBarButton && !ThemeUtils.TOOL_FOCUS;
        boolean isOk = isFileChooserButton || !ThemeUtils.BUTTON_FOCUS;
        if (isOk || isToolButtonFocus) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        Rectangle focusRect = b.getBounds();

        g.setColor(Color.black);
        g2d.setStroke(FOCUS_STROKE);

        int x1 = 2;
        int y1 = 2;
        int x2 = x1 + focusRect.width - 5;
        int y2 = y1 + focusRect.height - 5;

        if (!isToolBarButton) {
            x1++;
            y1++;
            x2--;
            y2--;
        }
    }

    /**
     * 创建UI
     *
     * @param c 组件
     * @return 创建的UI
     */
    public static ComponentUI createUI(final JComponent c) {
        return BUTTON_UI;
    }

    protected void paintButtonPressed(Graphics g, AbstractButton button) {
        if (isToolBarButton || isFileChooserButton) {
            return;
        }

        Color col = null;
        if (!ComparatorUtils.equals(button.getBackground(), ThemeUtils.BUTTON_NORMAL_COLOR)) {
            col = ColorRoutines.darken(button.getBackground(), BG_CHANGE_AMOUNT);
        } else {
            col = ThemeUtils.BUTTON_PRESS_COLOR;
        }

        g.setColor(col);

        drawXpButton(g, button, col, false);
        if (!(button instanceof JToggleButton)) {
            // Changed in 1.3.04: If button is icon-only
            // then don't shift
            if (ThemeUtils.SHIFT_BUTTON_TEXT && button.getText() != null && !"".equals(button.getText())) {
                g.translate(1, 1);
                graphicsTranslated = true;
            }
        }
    }

    public void paintToolBarButton(Graphics g, AbstractButton b) {
        Color col = null;

        // New in 1.3.7
        boolean isRollover = b.getModel().isRollover() || b.getModel().isArmed();
        Color toolButtColor = null;

        if (isFileChooserButton) {
            toolButtColor = b.getParent().getBackground();
        } else {
            toolButtColor = ThemeUtils.TOOLBUTT_COLOR;
        }

        if (b.getModel().isPressed()) {
            if (isRollover) {
                col = ThemeUtils.TOOLBUTT_PRESSED_COLOR;
            } else {
                if (b.isSelected()) {
                    col = ThemeUtils.TOOLBUTT_SELECTED_COLOR;
                } else {
                    col = toolButtColor;
                }
            }
        } else if (isRollover) {
            if (b.isSelected()) {
                col = ThemeUtils.TOOLBUTT_SELECTED_COLOR;
            } else {
                col = ThemeUtils.TOOLBUTT_ROLLOVER_COLOR;
            }
        } else if (b.isSelected()) {
            col = ThemeUtils.TOOLBUTT_SELECTED_COLOR;
        } else {
            col = toolButtColor;
        }
        g.setColor(col);
        drawXpToolBarButton(g, b, col, false);
    }

    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;
        if (isToolBarButton || isFileChooserButton) {
            paintToolBarButton(g, button);
            // the base class may paint text and/or icons
            super.paint(g, c);
            return;
        }
        if ((button instanceof JToggleButton) && button.isSelected()) {
            paintButtonPressed(g, button);
            // the base class may paint text and/or icons
            super.paint(g, c);
            return;
        }
        isDefault = (c instanceof JButton) && (((JButton) c).isDefaultButton());
        boolean isRollover = button.getModel().isRollover() && ThemeUtils.BUTTON_ROLLOVER;
        boolean isDefinedBackground = ComparatorUtils.equals(c.getBackground(), ThemeUtils.BUTTON_NORMAL_COLOR);
        Color col = null;
        if (!button.isEnabled()) {
            col = ThemeUtils.BUTTON_DISABLE_COLOR;
        } else if (button.getModel().isPressed()) {
            if (isRollover) {
                if (isDefinedBackground) {
                    col = ThemeUtils.BUTTON_PRESS_COLOR;
                } else {
                    col = ColorRoutines.darken(c.getBackground(), BG_CHANGE_AMOUNT);
                }
            } else {
                // button pressed but mouse exited
                col = c.getBackground();
            }
        } else if (isRollover) {
            if (isDefinedBackground) {
                col = ThemeUtils.BUTTON_ROLLOVER_BG_COLOR;
            } else {
                col = ColorRoutines.lighten(c.getBackground(), BG_CHANGE_AMOUNT);
            }
        } else {
            col = c.getBackground();
        }
        g.setColor(col);
        drawXpButton(g, button, col, isRollover);
        // the base class may paint text and/or icons
        super.paint(g, c);
    }

    // this overrides BasicButtonUI.paintIcon(...)
    protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
        if (c instanceof JToggleButton) {
            paintToggleButtonIcon(g, c, iconRect);
        } else {
            super.paintIcon(g, c, iconRect);
        }
    }

    protected void paintToggleButtonIcon(Graphics g, JComponent c, Rectangle iconRect) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        Icon icon = null;

        if (!model.isEnabled()) {
            if (model.isSelected()) {
                icon = (Icon) b.getDisabledSelectedIcon();
            } else {
                icon = (Icon) b.getDisabledIcon();
            }
        } else if (model.isPressed() && model.isArmed()) {
            icon = (Icon) b.getPressedIcon();
            if (icon == null) {
                // Use selected icon
                icon = (Icon) b.getSelectedIcon();
            }
        } else if (model.isSelected()) {
            if (b.isRolloverEnabled() && model.isRollover()) {
                icon = (Icon) b.getRolloverSelectedIcon();
                if (icon == null) {
                    icon = (Icon) b.getSelectedIcon();
                }
            } else {
                icon = (Icon) b.getSelectedIcon();
            }
        } else if (model.isRollover()) {
            icon = (Icon) b.getRolloverIcon();
        }

        if (icon == null) {
            icon = (Icon) b.getIcon();
        }

        icon.paintIcon(b, g, iconRect.x, iconRect.y);
    }

    public void update(Graphics g, JComponent c) {

        isToolBarButton = ComparatorUtils.equals(Boolean.TRUE, c.getClientProperty(UIToolBarUI.IS_TOOL_BAR_BUTTON_KEY));
        isFileChooserButton = ComparatorUtils.equals(Boolean.TRUE, c.getClientProperty("JFileChooser.isFileChooserButton"));
        paint(g, c);
        graphicsTranslated = false;
    }

    private void drawXpButton(Graphics g, AbstractButton b, Color c, boolean isRollover) {
        if (!b.isContentAreaFilled()) {
            return;
        }
        if (!b.isOpaque()) {
            return;
        }
        int w = b.getWidth();
        int h = b.getHeight();

        // paint border background
        if (b.getParent() != null) {
            Color bg = b.getParent().getBackground();
            g.setColor(bg);
        }
        g.drawRect(0, 0, w - 1, h - 1);

        Color normalLightColor = ThemeUtils.NORMAL_LIGHT_COLOR;
        Color normalDarkColor = ThemeUtils.NORMAL_DARK_COLOR;
        Graphics2D g2 = (Graphics2D) g;
        drawHighLightButton(g2, normalLightColor, normalDarkColor, w, h);

        // 1 pixel away from each corner
        if (isRollover) {
            g2.setColor(ThemeUtils.BUTTON_ROLLOVER_COLOR);
            g2.drawLine(1, h - 2, 1, h - 2);
            g2.drawLine(w - 2, h - 2, w - 2, h - 2);
            Color rolloverLightColor = ThemeUtils.ROLLOVER_LIGHT_COLOR;
            Color rolloverDarkColor = ThemeUtils.ROLLOVER_DARK_COLOR;
            drawHighLightButton(g2, rolloverLightColor, rolloverDarkColor, w, h);

        } else if (isDefault) {
            g2.setColor(ThemeUtils.BUTTON_DEFAULT_COLOR);
            g2.drawLine(1, h - 2, 1, h - 2);
            g2.drawLine(w - 2, h - 2, w - 2, h - 2);
            Color defaultLightColor = ThemeUtils.DEFAULT_LIGHT_COLOR;
            Color defaultDarkColor = ThemeUtils.DEFAULT_DARK_COLOR;
            drawHighLightButton(g2, defaultLightColor, defaultDarkColor, w, h);
        }
    }

    //harry: 画带有高光的按钮。
    private void drawHighLightButton(Graphics2D g2, Color color1, Color color2, int w, int h) {
        GradientPaint buttonPaint = new GradientPaint(0, 0, color1, 0, h - 1f, color2);
        GradientPaint buttonHighLightPaint = new GradientPaint(0, 0, new Color(1.0f, 1.0f, 1.0f, 0.6f), 0, h / 2f, new Color(1.0f, 1.0f, 1.0f, 0.2f));
        GradientPaint buttonHighLightLinePaint = new GradientPaint(1, 1, new Color(1.0f, 1.0f, 1.0f, 0.8f), 0, h / 2f, new Color(1.0f, 1.0f, 1.0f, 0.4f));
        g2.setPaint(buttonPaint);
        g2.fillRoundRect(0, 0, w - 1, h - 1, 3, 3);
        g2.setPaint(buttonHighLightLinePaint);//按钮内侧高光线（内发光，0%阻塞）
        g2.drawRoundRect(1, 1, w - 3, h - 3, 2, 2);
        g2.setPaint(buttonHighLightPaint);
        g2.fillRoundRect(0, 0, w - 1, h / 2, 3, 3);//按钮高光，覆盖按钮的上半部分。
    }


    private void drawXpToolBarButton(Graphics g, AbstractButton b, Color c, boolean isPressed) {
        int w = b.getWidth();
        int h = b.getHeight();
        b.setOpaque(false);

        if (b.isContentAreaFilled()) {
            g.fillRect(1, 1, w - 2, h - 2);
        }

    }
}