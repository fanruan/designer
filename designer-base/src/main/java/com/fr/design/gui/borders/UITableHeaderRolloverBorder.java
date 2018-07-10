/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.borders;

import com.fr.design.utils.ColorRoutines;
import com.fr.design.utils.ThemeUtils;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-6
 * Time: 下午3:36
 */
public class UITableHeaderRolloverBorder extends UITableHeaderBorder {

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        if (color1 == null) {
            color1 = ThemeUtils.TABLE_HEADER_ROLLOVER_COLOR;
            color2 = ColorRoutines.lighten(color1, 25);
        }

        g.setColor(color1);
        g.drawLine(x, y + h - 3, x + w - 1, y + h - 3);    // top
        g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);    // bottom

        g.setColor(color2);
        g.drawLine(x, y + h - 2, x + w - 1, y + h - 2);    // mid

        // don't paint separator
    }

}