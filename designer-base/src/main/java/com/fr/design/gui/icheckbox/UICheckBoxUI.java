package com.fr.design.gui.icheckbox;


import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalCheckBoxUI;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-13
 * Time: 下午5:12
 */
public class UICheckBoxUI extends MetalCheckBoxUI {

    /**
     * The Cached UI delegate.
     */
    private final static UICheckBoxUI CHECK_BOX_UI = new UICheckBoxUI();

    /**
     * 创建UI
     * @param c 组件
     * @return 返回组件UI
     */
    public static ComponentUI createUI(JComponent c) {
        return CHECK_BOX_UI;
    }

    /**
     * Installs some default values for the given button.
     *
     * @param button The reference of the button to install its default values.
     */

    static UICheckBoxIcon checkIcon = new UICheckBoxIcon();

    /**
     * 为组件加载图标
     * @param button 按钮
     */
    public void installDefaults(AbstractButton button) {
        super.installDefaults(button);
        icon = checkIcon;
        button.setRolloverEnabled(true);

        if (!ThemeUtils.BUTTON_ENTER){
            return;
        }
        if (!button.isFocusable()){
            return;
        }

        InputMap km = (InputMap) UIManager.get(getPropertyPrefix() + "focusInputMap");

        if (km != null) {
            km.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "pressed");
            km.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "released");
        }
    }

    static BasicStroke focusStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[]{1.0f}, 1.0f);

    protected void paintFocus(Graphics g, Rectangle t, Dimension arg2) {
        if (!ThemeUtils.BUTTON_FOCUS) {
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