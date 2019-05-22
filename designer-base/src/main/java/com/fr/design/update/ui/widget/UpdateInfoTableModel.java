package com.fr.design.update.ui.widget;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class UpdateInfoTableModel extends AbstractTableModel {
    private String[] titles;
    private List<Object[]> data;

    public UpdateInfoTableModel(String[] titles, List<Object[]> data) {
        this.titles = titles;
        this.data = data;
    }

    public void populateBean(List<Object[]> data) {
        if (data == null) {
            return;
        }
        clear();
        this.data = data;
        fireTableDataChanged();
    }

    public void clear() {
        data.clear();
    }

    public List<Object[]> updateBean() {
        return data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return titles.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return titles[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
    }
}

