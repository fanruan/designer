/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.icontainer;

import com.fr.design.utils.ThemeUtils;

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-6
 * Time: 上午11:28
 */
public class UITableScrollPaneBorder extends AbstractBorder implements UIResource {

    private static final Insets INSETS = new Insets(1, 1, 1, 1);

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        g.setColor(ThemeUtils.TABLE_BORDER_LIGHT_COLOR);
        g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);        // right
        g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);        // bottom

        g.setColor(ThemeUtils.TABLE_BORDER_DARK_COLOR);
        g.drawLine(x, y, x, y + h - 1);        // left
        g.drawLine(x, y, x + w - 1, y);        // top
    }

    public Insets getBorderInsets(Component c) {
        return INSETS;
    }

}