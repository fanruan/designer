package com.fr.design.chart.report;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.general.Inter;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-23 上午10:31:22
 */
public class MapReportEditor extends AbstractCellEditor implements TableCellEditor {
	private ValueEditorPane cellEditor;
	
	private List<ChangeListener> list = new ArrayList<ChangeListener>();
	
	public MapReportEditor() {
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		cellEditor = createPane();
		cellEditor.populate(value == null ? "" : value);
		return cellEditor;
	}

	public Object getCellEditorValue() {
		return cellEditor.update();
	}
	
	public void addChangeListener(ChangeListener l) {
		list.add(l);
	}
	
	private ValueEditorPane createPane() {
		TextEditor textEidtor = new TextEditor();
		initListeners(textEidtor);
		
		FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
		initListeners(formulaEditor);
		
		Editor[] editors = new Editor[]{textEidtor, formulaEditor};
		cellEditor = ValueEditorPaneFactory.createValueEditorPane(editors);
		return cellEditor;
	}
	
	private void initListeners(Editor editor) {
		for(int i = 0; i < list.size(); i++) {
			editor.addChangeListener(list.get(i));
		}
	}
}