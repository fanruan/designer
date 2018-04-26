package com.fr.design.present;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.fr.general.Inter;
import com.fr.stable.ColumnRow;

public class ColumnRowTableModel extends AbstractTableModel {

	private String[] columnNames = new String[] { Inter.getLocText("Column"), Inter.getLocText("Row") };
	private java.util.List<ColumnRow> columnRowList = new ArrayList<ColumnRow>();

	public ColumnRowTableModel() {
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return columnRowList.size();
	}

	public Object getValueAt(int row, int col) {
		ColumnRow cr = columnRowList.get(row);

		switch (col) {
		case 0:
			return new Integer(cr.getColumn());
		case 1:
			return new Integer(cr.getRow());
		}

		return null;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		ColumnRow cr = columnRowList.get(row);
		if (col == 0 && value instanceof Integer) {
			int c = ((Integer)value).intValue();
			int r = cr.getRow();
			cr = ColumnRow.valueOf(c, r);
		} else if (col == 1 && value instanceof Integer) {
			int c = cr.getColumn();
			int r = ((Integer)value).intValue();
			cr = ColumnRow.valueOf(c, r);
		}
		columnRowList.remove(row);
		columnRowList.add(row, cr);
	}

	@Override
	public Class getColumnClass(int c) {

		if (c == 0 || c == 1)
			return Integer.class;
		return String.class;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void addColumnRow(ColumnRow cr) {
		this.columnRowList.add(cr);
	}

	public void removeColumnRow(int index) {
		this.columnRowList.remove(index);
	}

	public ColumnRow getColumnRow(int index) {
		return this.columnRowList.get(index);
	}

	public void setColumnRow(ColumnRow cr, int index) {
		if (columnRowList.get(index) != null)
			columnRowList.remove(index);
		columnRowList.add(index, cr);
	}

	public void removeAllColumnRow() {
		this.columnRowList.clear();
	}
}