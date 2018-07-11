package com.fr.design.gui.ilist;

import javax.swing.CellEditor;

public interface ListCellEditor extends CellEditor {
	public java.awt.Component getListCellEditorComponent(
			javax.swing.JList list, java.lang.Object value, boolean isSelected, int index);
}