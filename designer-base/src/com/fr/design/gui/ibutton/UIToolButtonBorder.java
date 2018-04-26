/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.ibutton;

import com.fr.general.ComparatorUtils;
import com.fr.design.utils.DrawRoutines;
import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-19
 * Time: 下午4:39
 */
public class UIToolButtonBorder extends AbstractBorder {
    protected final Insets insets = new Insets(1, 1, 1, 1);

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
        AbstractButton b = (AbstractButton) c;
        Color col = null;

        boolean isFileChooserButton =ComparatorUtils.equals(Boolean.TRUE, b.getClientProperty("JFileChooser.isFileChooserButton"));

        // New in 1.3.7 (previously only b.getModel().isRollover() evaluated)
        boolean isRollover = b.getModel().isRollover() || b.getModel().isArmed();

        if (b.getModel().isPressed()) {
            if (isRollover) {
                col = ThemeUtils.TOOL_BORDER_PRESSED_COLOR;
            } else {
                if (b.isSelected()) {
                    col = ThemeUtils.TOOL_BORDER_SELECTED_COLOR;
                } else {
                    if (isFileChooserButton) {
                        return;    // no border painted
                    }

                    col = ThemeUtils.TOOL_BORDER_COLOR;
                    return;
                }
            }
        } else if (isRollover) {
            if (b.isSelected()) {
                col = ThemeUtils.TOOL_BORDER_SELECTED_COLOR;
            } else {
                col = ThemeUtils.TOOL_BORDER_ROLLOVER_COLOR;
            }
        } else if (b.isSelected()) {
            col = ThemeUtils.TOOL_BORDER_SELECTED_COLOR;
        } else {
            if (isFileChooserButton) {
                return;    // no border painted
            }

            col = ThemeUtils.TOOL_BORDER_COLOR;
            return;
        }
        DrawRoutines.drawRoundedBorder(g, col, x, y, w, h);
    }

    /**
     * Gets the border insets for a given component.
     *
     * @return some insets...
     */
    public Insets getBorderInsets(Component c) {
        if (!(c instanceof AbstractButton)){
            return insets;
        }

        AbstractButton b = (AbstractButton) c;

        if (b.getMargin() == null || (b.getMargin() instanceof UIResource)) {
            return new Insets(ThemeUtils.TOOL_MARGIN_TOP, ThemeUtils.TOOL_MARGIN_LEFT,
                    ThemeUtils.TOOL_MARGIN_BOTTOM, ThemeUtils.TOOL_MARGIN_RIGHT);
        } else {
            Insets margin = b.getMargin();

            return new Insets(
                    margin.top + 1,
                    margin.left + 1,
                    margin.bottom + 1,
                    margin.right + 1);
        }
    }
}