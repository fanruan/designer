/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.properties;

import java.awt.Component;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;



/**
 * @author richer
 * @since 6.5.3
 */
public class FRBorderLayoutConstraintsRenderer extends UILabel implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            this.setText(com.fr.design.i18n.Toolkit.i18nText("BorderLayout-" + value));
        }
        return this;
    }

}