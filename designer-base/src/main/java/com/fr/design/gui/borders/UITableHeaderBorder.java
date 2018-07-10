/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.borders;

import com.fr.design.utils.ColorRoutines;
import com.fr.design.utils.ThemeUtils;

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-6
 * Time: 下午3:37
 */
public class UITableHeaderBorder extends AbstractBorder implements UIResource {
    protected static final Insets INSETS_XP = new Insets(3, 0, 3, 2);
    protected Color color1, color2, color3, color4, color5;

    public Insets getBorderInsets(Component c) {
        return INSETS_XP;

    }

    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = INSETS_XP.left;
        insets.top = INSETS_XP.top;
        insets.right = INSETS_XP.right;
        insets.bottom = INSETS_XP.bottom;

        return insets;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        if (color1 == null) {
            color1 = ColorRoutines.darken(c.getBackground(), 5);
            color2 = ColorRoutines.darken(c.getBackground(), 10);
            color3 = ColorRoutines.darken(c.getBackground(), 15);
            color4 = ThemeUtils.TABLE_HEADER_DARK_COLOR;
            color5 = ThemeUtils.TABLE_HEADER_LIGHT_COLOR;
        }

        // paint 3 bottom lines
        g.setColor(color1);
        g.drawLine(x, y + h - 3, x + w - 1, y + h - 3);

        g.setColor(color2);
        g.drawLine(x, y + h - 2, x + w - 1, y + h - 2);

        g.setColor(color3);
        g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);

        // paint separator
        g.setColor(color4);
        g.drawLine(x + w - 2, y + 3, x + w - 2, y + h - 5);

        g.setColor(color5);
        g.drawLine(x + w - 1, y + 3, x + w - 1, y + h - 5);

    }
}