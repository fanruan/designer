package com.fr.design.gui.ibutton;


import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalRadioButtonUI;
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.text.View;
import sun.swing.SwingUtilities2;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-13
 * Time: 下午2:50
 */
public class UIRadioButtonUI extends MetalRadioButtonUI {

    Component c;

    /**
     * the only instance of the radiobuttonUI
     */
    private static final UIRadioButtonUI RADIO_BUTTON_UI = new UIRadioButtonUI();

    /* the only instance of the stroke for the focus */
    private static BasicStroke focusStroke =
            new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                    1.0f, new float[]{1.0f, 1.0f}, 0.0f);

    /* the only instance of the radiobutton icon*/
    private static UIRadioButtonIcon radioButton;

    /**
     * 创建UI
     * @param c 组件
     * @return 返回本类对象
     */
    public static ComponentUI createUI(JComponent c) {
        if (c instanceof JRadioButton) {
            JRadioButton jb = (JRadioButton) c;
            jb.setRolloverEnabled(true);
        }

        return RADIO_BUTTON_UI;
    }

    /**
     * 为该组件加载图标
     * @param c 组件对象
     */
    public void installUI(JComponent c) {
        super.installUI(c);

        icon = getRadioButton();

        if (!ThemeUtils.BUTTON_ENTER) {
            return;
        }
        if (!c.isFocusable()){
            return;
        }

        InputMap km = (InputMap) UIManager.get(getPropertyPrefix() + "focusInputMap");

        if (km != null) {
            km.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "pressed");
            km.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "released");
        }
    }

    /**
     * Returns the skinned Icon
     *
     * @return TinyRadioButtonIcon
     */
    protected UIRadioButtonIcon getRadioButton() {
        if (radioButton == null) {
            radioButton = new UIRadioButtonIcon();
        }

        return radioButton;
    }

    // ********************************
    //        Paint Methods
    // ********************************
    public synchronized void paint(Graphics g, JComponent c) {

        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        Dimension size = c.getSize();

        int w = size.width;
        int h = size.height;

        Font f = c.getFont();
        g.setFont(f);
        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, f);

        Rectangle viewRect = new Rectangle(size);
        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();

        Insets i = c.getInsets();
        viewRect.x += i.left;
        viewRect.y += i.top;
        viewRect.width -= (i.right + viewRect.x);
        viewRect.height -= (i.bottom + viewRect.y);

        Icon altIcon = b.getIcon();
        Icon selectedIcon = null;
        Icon disabledIcon = null;

        String text = SwingUtilities.layoutCompoundLabel(
                c, fm, b.getText(), altIcon != null ? altIcon : getDefaultIcon(),
                b.getVerticalAlignment(), b.getHorizontalAlignment(),
                b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
                viewRect, iconRect, textRect, b.getIconTextGap());

        // fill background
        if (c.isOpaque()) {
            g.setColor(b.getBackground());
            g.fillRect(0, 0, size.width, size.height);
        }


        // Paint the radio button
        if (altIcon != null) {

            if (!model.isEnabled()) {
                if (model.isSelected()) {
                    altIcon = b.getDisabledSelectedIcon();
                } else {
                    altIcon = b.getDisabledIcon();
                }
            } else if (model.isPressed() && model.isArmed()) {
                altIcon = b.getPressedIcon();
                if (altIcon == null) {
                    // Use selected icon
                    altIcon = b.getSelectedIcon();
                }
            } else if (model.isSelected()) {
                if (b.isRolloverEnabled() && model.isRollover()) {
                    altIcon = b.getRolloverSelectedIcon();
                    if (altIcon == null) {
                        altIcon = b.getSelectedIcon();
                    }
                } else {
                    altIcon = b.getSelectedIcon();
                }
            } else if (b.isRolloverEnabled() && model.isRollover()) {
                altIcon = b.getRolloverIcon();
            }

            if (altIcon == null) {
                altIcon = b.getIcon();
            }

            altIcon.paintIcon(c, g, iconRect.x, iconRect.y);

        } else {
            getDefaultIcon().paintIcon(c, g, iconRect.x, iconRect.y);
        }

        // Draw the Text
        if (text != null) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, textRect);
            } else {
                int mnemIndex = b.getDisplayedMnemonicIndex();
                if (model.isEnabled()) {
                    // *** paint the text normally
                    g.setColor(b.getForeground());
                } else {
                    // *** paint the text disabled
                    g.setColor(getDisabledTextColor());
                }
                if (markMnemonic(text, mnemIndex)) {
                    SwingUtilities2.drawStringUnderlineCharAt(c, g, text,
                            mnemIndex, textRect.x, textRect.y + fm.getAscent());
                } else {
                    SwingUtilities2.drawString(c, g, text, textRect.x, textRect.y + fm.getAscent());
                }
            }
            if (b.hasFocus() && b.isFocusPainted() &&
                    textRect.width > 0 && textRect.height > 0) {
                paintFocus(g, textRect, size);
            }
        }
    }

    /**
     * @param text
     * @param mnemIndex 助记符在text中的索引
     * @return true:需要给助记符画一个下划线
     */
    private boolean markMnemonic(String text, int mnemIndex) {
        return mnemIndex > 0 && text != null && text.length() > 0 && text.length() > mnemIndex;
    }

    /**
     * Paints the focus for the radiobutton
     *
     * @see javax.swing.plaf.metal.MetalRadioButtonUI#paintFocus(java.awt.Graphics, java.awt.Rectangle, java.awt.Dimension)
     */
    protected void paintFocus(Graphics g, Rectangle t, Dimension arg2) {
        if (!ThemeUtils.BUTTON_FOCUS){
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.setStroke(focusStroke);

        int x1 = t.x - 1;
        int y1 = t.y - 1;
        int x2 = x1 + t.width + 1;
        int y2 = y1 + t.height + 1;

        g2d.drawLine(x1, y1, x2, y1);
        g2d.drawLine(x1, y1, x1, y2);
        g2d.drawLine(x1, y2, x2, y2);
        g2d.drawLine(x2, y1, x2, y2);
    }
}