package com.fr.poly.group;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.mainframe.widget.editors.StringEditor;
import com.fr.general.Inter;
import com.fr.report.poly.TemplateBlock;

public class PolyNameGroup implements GroupModel {
	private TemplateBlock block;
	private DefaultTableCellRenderer defaultTableCellRenderer;
	private PropertyCellEditor defaultCellEditor;

	public PolyNameGroup(TemplateBlock block) {
		this.block = block;
		this.defaultCellEditor = new PropertyCellEditor(new StringEditor());
		this.defaultTableCellRenderer = new DefaultTableCellRenderer();
	}

	@Override
	public String getGroupName() {
		return Inter.getLocText("Form-Basic_Properties");
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public TableCellRenderer getRenderer(int row) {
		return defaultTableCellRenderer;
	}

	@Override
	public TableCellEditor getEditor(int row) {
		return defaultCellEditor;
	}

	@Override
	public Object getValue(int row, int column) {
		if (column == 0) {
			return Inter.getLocText("Poly_Name");
		} else {
			return block.getBlockName();
		}
	}

	@Override
	public boolean setValue(Object value, int row, int column) {
		if(column==1){
			block.setBlockName((String) value);
			return true;
		}else{
		return false;}
	}

	@Override
	public boolean isEditable(int row) {
		return true;
	}
}