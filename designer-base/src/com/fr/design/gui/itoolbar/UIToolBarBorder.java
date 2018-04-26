/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.itoolbar;

import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-6
 * Time: 上午9:57
 */
public class UIToolBarBorder extends MetalBorders.ToolBarBorder {

    public Insets getBorderInsets(Component c) {
        return getBorderInsets(c, new Insets(0, 0, 0, 0));
    }

    public Insets getBorderInsets(Component c, Insets newInsets) {
        newInsets.top = newInsets.left = newInsets.bottom = newInsets.right = 2;

        // we cannot assume that c is a JToolBar
        if (!(c instanceof JToolBar)){
            return newInsets;
        }

        if (((JToolBar) c).isFloatable()) {
            if (((JToolBar) c).getOrientation() == HORIZONTAL) {
                if (c.getComponentOrientation().isLeftToRight()) {
                    newInsets.left = UIToolBarUI.FLOATABLE_GRIP_SIZE + 2;
                } else {
                    newInsets.right = UIToolBarUI.FLOATABLE_GRIP_SIZE + 2;
                }
            } else { // vertical
                newInsets.top = UIToolBarUI.FLOATABLE_GRIP_SIZE + 2;
            }
        }

        Insets margin = ((JToolBar) c).getMargin();

        if (margin != null) {
            newInsets.left += margin.left;
            newInsets.top += margin.top;
            newInsets.right += margin.right;
            newInsets.bottom += margin.bottom;
        }

        return newInsets;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        if (!(c instanceof JToolBar)){
            return;
        }
        drawWinBorder(c, g, x, y, w, h);
        if (((JToolBar) c).getOrientation() == HORIZONTAL) {
            g.setColor(ThemeUtils.TOOL_BAR_LIGHT_COLOR);
            g.drawLine(x, y, w - 1, y);                // top
            g.setColor(ThemeUtils.TOOL_BAR_DARK_COLOR);
            g.drawLine(x, h - 1, w - 1, h - 1);        // bottom
        } else {
            g.setColor(ThemeUtils.TOOL_BAR_LIGHT_COLOR);
            g.drawLine(x, y, x, h - 1);                // left
            g.setColor(ThemeUtils.TOOL_BAR_DARK_COLOR);
            g.drawLine(w - 1, y, w - 1, h - 1);        // right
        }
    }

    protected void drawWinBorder(Component c, Graphics g, int x, int y, int w, int h) {
        g.translate(x, y);

        if (((JToolBar) c).isFloatable()) {
            // paint grip
            if (((JToolBar) c).getOrientation() == HORIZONTAL) {
                int xoff = 3;
                if (!c.getComponentOrientation().isLeftToRight()) {
                    xoff = c.getBounds().width - UIToolBarUI.FLOATABLE_GRIP_SIZE + 3;
                }

                g.setColor(ThemeUtils.TOOL_GRIP_LIGHT_COLOR);
                g.drawLine(xoff, 3, xoff + 1, 3);
                g.drawLine(xoff, 3, xoff, h - 5);
                g.setColor(ThemeUtils.TOOL_GRIP_DARK_COLOR);
                g.drawLine(xoff, h - 4, xoff + 1, h - 4);
                g.drawLine(xoff + 2, 3, xoff + 2, h - 4);
            } else { // vertical
                g.setColor(ThemeUtils.TOOL_GRIP_LIGHT_COLOR);
                g.drawLine(3, 3, 3, 4);
                g.drawLine(3, 3, w - 4, 3);
                g.setColor(ThemeUtils.TOOL_GRIP_DARK_COLOR);
                g.drawLine(w - 4, 4, w - 4, 5);
                g.drawLine(3, 5, w - 4, 5);
            }
        }

        g.translate(-x, -y);
    }

}