/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.menu;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-19
 * Time: 下午4:31
 */
public class UISeparatorUI extends BasicSeparatorUI {
    protected static final Dimension VERT_DIMENSION = new Dimension(0, 2);
    protected static final Dimension HORZ_DIMENSION = new Dimension(2, 0);

    /**
     * 创建组件UI
     * @param c 组件
     * @return 组件UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new UISeparatorUI();
    }

    protected void installDefaults(JSeparator s) {
        LookAndFeel.installColors(s, "Separator.background", "Separator.foreground");
    }

    public void paint(Graphics g, JComponent c) {
        Dimension s = c.getSize();
        g.setColor(c.getBackground());

        if (((JSeparator) c).getOrientation() == JSeparator.VERTICAL) {
            g.drawLine(0, 0, 0, s.height);
        } else { // HORIZONTAL
            g.drawLine(0, 0, s.width, 0);
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        if (((JSeparator) c).getOrientation() == JSeparator.VERTICAL) {
            return HORZ_DIMENSION;
        } else {
            return VERT_DIMENSION;
        }
    }

    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }
}