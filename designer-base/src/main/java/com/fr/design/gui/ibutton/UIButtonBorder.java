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
 * Time: 上午10:30
 */
public class UIButtonBorder extends AbstractBorder implements UIResource {
    protected final Insets borderInsets = new Insets(2, 2, 2, 2);

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
        drawXpBorder(c, g, x, y, w, h);
    }


    private void drawXpBorder(Component c, Graphics g, int x, int y, int w, int h) {
        AbstractButton b = (AbstractButton) c;
        boolean isDefault = (c instanceof JButton) && (((JButton) c).isDefaultButton());
        boolean isComboBoxButton = ComparatorUtils.equals(Boolean.TRUE,b.getClientProperty("isComboBoxButton"));
        if (isComboBoxButton) {
            if (!b.isEnabled()) {
                DrawRoutines.drawRoundedBorder(
                        g, ThemeUtils.COMBO_BORDER_DISABLED_COLOR, x, y, w, h);
            } else {
                DrawRoutines.drawRoundedBorder(
                        g, ThemeUtils.COMBO_BORDER_COLOR, x, y, w, h);

                if (b.getModel().isPressed()){
                    return;
                }
            }
        } else {    // it's a normal JButton or a JSpinner button
            boolean isSpinnerButton = ComparatorUtils.equals(Boolean.TRUE,b.getClientProperty("isSpinnerButton")) ;
            if (!b.isEnabled()) {
                DrawRoutines.drawRoundedBorder(
                        g, ThemeUtils.BUTTON_BORDER_DISABLE_COLOR, x, y, w, h);
            } else {
                DrawRoutines.drawRoundedBorder(
                        g, ThemeUtils.BUTTON_BORDER_COLOR, x, y, w, h);

                if (b.getModel().isPressed()){
                    return;
                }

            }
        }
    }

    /**
     * Gets the border insets for a given component.
     *
     * @param c The component to get its border insets.
     * @return Always returns the same insets as defined in <code>insets</code>.
     */
    public Insets getBorderInsets(Component c) {
        return borderInsets;
    }
}