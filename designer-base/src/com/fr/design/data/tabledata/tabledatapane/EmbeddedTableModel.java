package com.fr.design.data.tabledata.tabledatapane;

import javax.swing.table.AbstractTableModel;

import com.fr.data.impl.EmbeddedTableData;

public class EmbeddedTableModel extends AbstractTableModel {
    private EmbeddedTableData embeddedTableData;

    public EmbeddedTableModel(EmbeddedTableData editableTableData) {
        this.embeddedTableData = editableTableData;
    }

    public EmbeddedTableData getEditableTableData() {
        return embeddedTableData;
    }

    public void setEditableTableData(EmbeddedTableData editableTableData) {
        this.embeddedTableData = editableTableData;
    }

    public String getColumnName(int column) {
    	if (column == 0) {
    		return "";
    	} else {
    		return embeddedTableData.getColumnName(column - 1);
    	}
    }

    public Class getColumnClass(int column) {
    	if (column == 0) {
    		return String.class;
    	} else {
    		return embeddedTableData.getColumnClass(column - 1);
    	}
    }

    public int getRowCount() {
        return embeddedTableData.getRowCount();
    }

    public int getColumnCount() {
    	if (embeddedTableData.getColumnCount() == 0) {
    		return 1;
    	} else {
    		return embeddedTableData.getColumnCount() + 1;
    	}
    }

    public Object getValueAt(int row, int column) {
    	if (column == 0) {
    		return Integer.toString(row + 1);
    	} else {
    		return embeddedTableData.getValueAt(row, column - 1);
    	}
    }

    /**
     * Returns false.  This is the default implementation for all cells.
     *
     * @param rowIndex    the row being queried
     * @param columnIndex the column being queried
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	return columnIndex != 0;
    }

    /**
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	if (columnIndex != 0) {
    		this.embeddedTableData.setValueAt(aValue, rowIndex, columnIndex - 1);
    	}
    }

    /**
     * Add column.
     *
     * @param columnName column name
     * @param columnClass column class
     */
    public void addColumn(String columnName, Class columnClass) {
        embeddedTableData.addColumn(columnName, columnClass);
    }

    public void addNewRowData(int index) {
        embeddedTableData.addNewRow(index);
    }

    public void removeRow(int rowIndex) {
        embeddedTableData.removeRow(rowIndex);
    }
    
    public void clear() {
    	embeddedTableData.clear();
    }
}