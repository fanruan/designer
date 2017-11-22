package com.fr.design.gui.imenu;

import com.fr.design.constants.UIConstants;

import javax.swing.plaf.basic.BasicMenuBarUI;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-30
 * Time: 上午10:36
 */
public class UIMenuBarUI extends BasicMenuBarUI {

    public UIMenuBarUI() {
    }

    public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
        //是否完全不透明
        super.paint(g, c);
        if (!c.isOpaque()) {
            return;
        }
        Color oldColor = g.getColor();
        g.setColor(UIConstants.UI_MENU_BACKGOURND);
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
        g.setColor(oldColor);
    }


}