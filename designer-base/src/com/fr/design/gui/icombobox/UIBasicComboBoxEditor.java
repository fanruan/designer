/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.icombobox;

import com.fr.general.ComparatorUtils;
import com.fr.design.utils.DrawRoutines;
import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.metal.MetalComboBoxEditor;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-18
 * Time: 上午11:04
 */
public class UIBasicComboBoxEditor extends MetalComboBoxEditor {

    public UIBasicComboBoxEditor() {
        super();

        editor = new JTextField("", 9) {
            // workaround for 4530952
            public void setText(String s) {
                if (ComparatorUtils.equals(getText(),s)) {
                    return;
                }

                super.setText(s);
            }
        };

        editor.setBorder(new EditorBorder());
    }

    class EditorBorder extends AbstractBorder {

        /**
         * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
         */
        public Insets getBorderInsets(Component c) {
            // Note: I just adjusted insets until
            // editable and non-editable combo boxes look equal
            return new Insets(
                    1,
                    ThemeUtils.COMBO_INSETS.left + 1,
                    1,
                    0);
        }

        /**
         * @see javax.swing.border.Border#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
         */
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            JComponent combo = (JComponent) editor.getParent();

            if (combo.getBorder() == null) {
                return;
            }
            drawXpBorder(c, g, x, y, w, h);

        }
    }

    private void drawXpBorder(Component c, Graphics g, int x, int y, int w, int h) {
        // paint border background - next parent is combo box
        Color bg = c.getParent().getParent().getBackground();
        g.setColor(bg);
        g.drawLine(x, y, x + w - 1, y);                    // top
        g.drawLine(x, y, x, y + h - 1);                    // left
        g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);    // bottom

        if (!c.isEnabled()) {
            DrawRoutines.drawEditableComboBorder(
                    g, ThemeUtils.COMBO_BORDER_DISABLED_COLOR, 0, 0, w, h);
        } else {
            DrawRoutines.drawEditableComboBorder(
                    g, ThemeUtils.COMBO_BORDER_COLOR, 0, 0, w, h);
        }
    }


    /**
     * A subclass of BasicComboBoxEditor that implements UIResource.
     * BasicComboBoxEditor doesn't implement UIResource
     * directly so that applications can safely override the
     * cellRenderer property with BasicListCellRenderer subclasses.
     * <p/>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases. The current serialization support is
     * appropriate for short term storage or RMI between applications running
     * the same version of Swing.  As of 1.4, support for long term storage
     * of all JavaBeans<sup><font size="-2">TM</font></sup>
     * has been added to the <code>java.beans</code> package.
     * Please see {@link java.beans.XMLEncoder}.
     */
    public static class UIResource extends UIBasicComboBoxEditor
            implements javax.swing.plaf.UIResource {
    }
}