package com.fr.design.gui.itable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class UITableDataModel implements TableModel {
    protected List<Object[]> values;
    protected int columnSize;

    public UITableDataModel(int columnSize) {
        this.columnSize = columnSize;
        this.values = new ArrayList<Object[]>();
    }

    public UITableDataModel(List<Object[]> values) {
        if (values == null) {
            return;
        }
        this.values = values;
        this.columnSize = values.get(0).length;
    }

    public void populateBean(List<Object[]> values) {
        if (values == null) {
            return;
        }
        this.values.clear();
        this.values = values;
    }

    public void clear() {
        values.clear();
    }

    public List<Object[]> updateBean() {
        return values;
    }

    public void dragSort(int rowIndex, boolean positive) {
        if (values == null || rowIndex == -1) {
            return;
        }

        if (positive && rowIndex < values.size() - 1) {
            Object[] tmp = values.get(rowIndex);
            values.set(rowIndex, values.get(rowIndex + 1));
            values.set(rowIndex + 1, tmp);
            tmp = null;
        } else if (!positive && rowIndex > 1) {
            Object[] tmp = values.get(rowIndex);
            values.set(rowIndex, values.get(rowIndex - 1));
            values.set(rowIndex - 1, tmp);
            tmp = null;
        }
    }

    public Object[] getLine(int rowIndex) {
        if (rowIndex >= values.size() || rowIndex < 0) {
            return null;
        }
        return values.get(rowIndex);
    }

    public void removeLine(int rowIndex) {
        if (rowIndex >= values.size() || rowIndex < 0) {
            return;
        }
        values.remove(rowIndex);
    }

    public void addLine(Object[] line) {
        if (line.length != columnSize) {
            return;
        }
        values.add(line);
    }

    public void addBlankLine() {
        Object[] newLine = new Object[columnSize];
        values.add(newLine);
    }

    @Override
    public int getRowCount() {
        return values.size();
    }

    @Override
    public int getColumnCount() {
        return columnSize;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= values.size() || columnIndex >= columnSize) {
            return null;
        }
        Object[] rowValue = values.get(rowIndex);
        if (columnIndex > -1 && columnIndex < rowValue.length) {
            return values.get(rowIndex)[columnIndex];
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex >= values.size() || columnIndex >= columnSize) {
            return;
        }
        if (aValue == null) {
            values.get(rowIndex)[columnIndex] = null;
            return;
        }
        values.get(rowIndex)[columnIndex] = aValue.toString();
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        // TODO Auto-generated method stub

    }
}