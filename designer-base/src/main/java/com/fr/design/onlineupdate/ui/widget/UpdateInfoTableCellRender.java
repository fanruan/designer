package com.fr.design.onlineupdate.ui.widget;

import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class UpdateInfoTableCellRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setBackground((row & 1) != 0 ? new Color(0xf0f0f0) : Color.WHITE);
        if ((Boolean) table.getValueAt(row, 2)) {
            cell.setBackground(new Color(0xdfecfd));
        }
        if (column == 0) {
            //设置首列日期居中显示
            setHorizontalAlignment(JLabel.CENTER);

            for (int i = 1; row - i >= 0; i++) {
                if (ComparatorUtils.equals(table.getValueAt(row - i, 0), value)) {
                    //调用的父类JLabel的setText，table本身的值不变
                    setText(StringUtils.EMPTY);
                    break;
                }
            }
        }
        return cell;
    }

    @Override
    public void setForeground(Color c) {
        super.setForeground(c);
    }

    @Override
    public void setBackground(Color c) {
        super.setBackground(c);
    }
}
