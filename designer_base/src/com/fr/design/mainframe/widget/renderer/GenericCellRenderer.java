package com.fr.design.mainframe.widget.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public abstract class GenericCellRenderer extends JComponent implements TableCellRenderer {

    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	Component component = getRendererComponent();
        Color fg = null;
        Color bg = null;
        JTable.DropLocation dropLocation = table.getDropLocation();

        if ((dropLocation != null) && !dropLocation.isInsertRow()
            && !dropLocation.isInsertColumn()
            && (dropLocation.getRow() == row)
            && (dropLocation.getColumn() == column)) {
            fg = UIManager.getColor("Table.dropCellForeground");
            bg = UIManager.getColor("Table.dropCellBackground");

            isSelected = true;
        }

        if (isSelected) {
            component.setForeground((fg == null) ? table.getSelectionForeground() : fg);
            component.setBackground((bg == null) ? table.getSelectionBackground() : bg);
        } else {
            component.setForeground(table.getForeground());
            component.setBackground(table.getBackground());
        }

        component.setFont(table.getFont());

        if (hasFocus) {
            Border border = null;

            if (isSelected) {
                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
            }

            if (border == null) {
                border = UIManager.getBorder("Table.focusCellHighlightBorder");
            }

            ((JComponent) component).setBorder(border);

            if (!isSelected && table.isCellEditable(row, column)) {
                Color col;
                col = UIManager.getColor("Table.focusCellForeground");

                if (col != null) {
                    component.setForeground(col);
                }

                col = UIManager.getColor("Table.focusCellBackground");

                if (col != null) {
                    component.setBackground(col);
                }
            }
        }

        ((JComponent) component).setBorder(getNoFocusBorder());

        setValue(value);

        return component;
    }

    private static Border getNoFocusBorder() {
        if (System.getSecurityManager() != null) {
            return SAFE_NO_FOCUS_BORDER;
        } else {
            return noFocusBorder;
        }
    }
    
    public Component getRendererComponent() {
    	return this;
    }

    public abstract void setValue(Object value);
}