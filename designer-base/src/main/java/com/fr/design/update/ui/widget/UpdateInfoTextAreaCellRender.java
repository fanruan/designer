package com.fr.design.update.ui.widget;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class UpdateInfoTextAreaCellRender extends JTextArea implements TableCellRenderer {
    public UpdateInfoTextAreaCellRender() {
        setMargin(new Insets(2, 2, 2, 2));
        setLineWrap(true);
        setWrapStyleWord(true);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // 计算当前行的最佳高度
        int maxPreferredHeight = 0;
        for (int i = 0; i < table.getColumnCount(); i++) {
            setText("" + table.getValueAt(row, i));
            setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
            maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);
        }

        if (table.getRowHeight(row) != maxPreferredHeight) {
            table.setRowHeight(row, maxPreferredHeight);
        }

        setText(value == null ? "" : value.toString());
        setBackground((row & 1) != 0 ? new Color(0xf0f0f0) : Color.WHITE);
        if ((Boolean) table.getValueAt(row, 2)) {
            setBackground(new Color(0xdfecfd));
        }
        return this;
    }
}