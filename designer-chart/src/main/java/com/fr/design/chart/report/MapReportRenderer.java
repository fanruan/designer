package com.fr.design.chart.report;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.general.Inter;

/**
 * 简要提供 文本和公式两种编辑器
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-23 上午10:30:16
 */
public class MapReportRenderer extends DefaultTableCellRenderer {
	private ValueEditorPane cellEditor;
	
	public MapReportRenderer() {
		Editor[] editors = new Editor[]{new TextEditor(), new FormulaEditor(Inter.getLocText("Parameter-Formula"))};
		cellEditor = ValueEditorPaneFactory.createValueEditorPane(editors);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		cellEditor.populate(value == null ? "" : value);
		return cellEditor;
	}
}