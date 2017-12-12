package com.fr.design.gui.imenu;


import com.fr.design.constants.UIConstants;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalSeparatorUI;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-1-2
 * Time: 上午10:00
 */
public class UIPopupMenuSeparatorUI extends MetalSeparatorUI {
    public static ComponentUI createUI(JComponent c) {
        return new UIPopupMenuSeparatorUI();
    }


    public void paint(Graphics g, JComponent c) {
        Dimension s = c.getSize();
        g.setColor(UIConstants.UIPOPUPMENU_LINE_COLOR);
        g.drawLine(2, 1, s.width - 3, 1);
    }
}