/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.borders;

import com.fr.design.constants.UIConstants;
import com.fr.design.utils.DrawRoutines;
import com.fr.design.utils.ThemeUtils;

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-5
 * Time: 下午2:49
 */
public class UITextFieldBorder extends AbstractBorder implements UIResource {
    private Insets insets;

    public UITextFieldBorder() {
        insets = ThemeUtils.TEXT_INSETS;
    }

    public UITextFieldBorder(Insets insets) {
        this.insets = insets;
    }

    /**
     * Gets the border insets for a given component.
     *
     * @param c The component to get its border insets.
     * @return Always returns the same insets as defined in <code>insets</code>.
     */
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    /**
     * Use the skin to paint the border
     *
     * @see javax.swing.border.Border#paintBorder(Component, Graphics, int, int, int, int)
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        drawXpBorder(c, g, x, y, w, h);
    }

    private void drawXpBorder(Component c, Graphics g, int x, int y, int w, int h) {
        if (!c.isEnabled()) {
            DrawRoutines.drawBorder(
                    g, ThemeUtils.TEXT_BORDER_DISABLED_COLOR, x, y, w, h);
        } else {
            DrawRoutines.drawBorder(
                    g, UIConstants.POP_DIALOG_BORDER, x, y, w, h);
        }
    }
}