package com.fr.design.gui.imenutable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


public class UIMenuTableDataModel implements TableModel{
	private List<UIMenuNameableCreator> values;
	
	public UIMenuTableDataModel() {
		values = new ArrayList<UIMenuNameableCreator>();
	}

	
	public void populateBean(List<UIMenuNameableCreator> values) {
		if(values == null) {
			return;
		}
		this.values.clear();
		this.values = values;
	}
	
	public List<UIMenuNameableCreator> updateBean() {
		return values;
	}
	
	public void dragSort(int rowIndex, boolean positive) {
		if(values == null || rowIndex == -1) {
			return;
		}
		
		if(positive && rowIndex < values.size() - 1) {
			UIMenuNameableCreator tmp = values.get(rowIndex);
			values.set(rowIndex, values.get(rowIndex + 1));
			values.set(rowIndex + 1, tmp);
			tmp = null;
		} else if(!positive && rowIndex > 1) {
			UIMenuNameableCreator tmp = values.get(rowIndex);
			values.set(rowIndex, values.get(rowIndex - 1));
			values.set(rowIndex - 1, tmp);
			tmp = null;
		}
	}
	
	public UIMenuNameableCreator getLine(int rowIndex) {
		if(rowIndex >= values.size() || rowIndex < 0) {
			return null;
		}
		return values.get(rowIndex);
	}
	
	public void removeLine(int rowIndex) {
		if(rowIndex >= values.size() || rowIndex < 0) {
			return;
		}
		values.remove(rowIndex);
	}
	
	public void addLine(UIMenuNameableCreator line) {
		values.add(line);
	}
	
	@Override
	public int getRowCount() {
		return values.size();
	}

	@Override
	public int getColumnCount() {
		return 1;
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
		if(rowIndex >= values.size()) {
			return null;
		}
		return values.get(rowIndex).getName();
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(rowIndex >= values.size()) {
			return;
		}
		values.get(rowIndex).setName(aValue.toString());
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