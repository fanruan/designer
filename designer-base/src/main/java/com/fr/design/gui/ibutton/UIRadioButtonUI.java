package com.fr.design.gui.ibutton;


import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalRadioButtonUI;
import java.awt.*;
import java.awt.event.KeyEvent;

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