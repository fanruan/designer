package com.fr.design.gui.itable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.fr.base.BaseUtils;
import com.fr.stable.StringUtils;

public class GroupRenderer extends DefaultTableCellRenderer {

    private static final Icon PLUS_ICON = new ImageIcon(BaseUtils.readImage("com/fr/design/images/form/designer/properties/plus.png"));
    private static final Icon MINUS_ICON = new ImageIcon(BaseUtils.readImage("com/fr/design/images/form/designer/properties/minus.png"));

    public GroupRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
        String text = getText();
        if (StringUtils.isNotBlank(text)) {
            if (text.length() > 0 && text.charAt(0) == '+') {
                setIcon(PLUS_ICON);
                setText(text.substring(1));
            } else if (text.length() > 0 && text.charAt(0) == '-') {
                setIcon(MINUS_ICON);
                setText(text.substring(1));
            } else {
                setIcon(null);
                setText(text);
            }
        } else {
            setIcon(null);
            setText(text);
        }
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getGridColor());
            setForeground(Color.black);
        }
        return this;
    }
}