/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.iprogressbar;

import com.fr.design.utils.DrawRoutines;
import com.fr.design.utils.ThemeUtils;

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-4
 * Time: 上午11:03
 */
public class UIProgressBarBorder extends AbstractBorder implements UIResource {
    protected static final Insets INSETS_YQ = new Insets(3, 3, 3, 3);

    /**
     * Draws the button border for the given component.
     *
     * @param c The component to draw its border.
     * @param g The graphics context.
     * @param x The x coordinate of the top left corner.
     * @param y The y coordinate of the top left corner.
     * @param w The width.
     * @param h The height.
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        DrawRoutines.drawProgressBarBorder(g,
                ThemeUtils.PROCESS_BORDER_COLOR, x, y, w, h);

        DrawRoutines.drawProgressBarBorder(g,
                ThemeUtils.PROCESS_DARK_COLOR, x + 1, y + 1, w - 2, h - 2);


        w -= 4;
        h -= 4;
        x += 2;
        y += 2;
        g.setColor(ThemeUtils.PROCESS_LIGHT_COLOR);
        // rect
        g.drawLine(x + 1, y, x + w - 2, y);
        g.drawLine(x, y + 1, x, y + h - 2);

        // track
        g.setColor(ThemeUtils.PROCESS_TRACK_COLOR);
        g.drawLine(x + 1, y + h - 1, x + w - 2, y + h - 1);
        g.drawLine(x + w - 1, y + 1, x + w - 1, y + h - 2);

    }

    /**
     * Gets the border insets for a given component.
     *
     * @param c The component to get its border insets.
     * @return Always returns the same insets as defined in <code>insets</code>.
     */
    public Insets getBorderInsets(Component c) {
        return INSETS_YQ;
    }
}