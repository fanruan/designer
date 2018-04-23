package com.fr.design.designer.properties;

import java.awt.Component;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WCardLayout;
import com.fr.general.Inter;

public class CardLayoutConstraints implements ConstraintsGroupModel {

	private DefaultTableCellRenderer renderer;
	private FRCardConstraintsEditor editor1;
	private Widget widget;
	private WCardLayout layout;
	private XWCardLayout container;

	public CardLayoutConstraints(XWCardLayout parent, Component comp) {
		this.layout = parent.toData();
		this.container = parent;
		this.widget = ((XWidgetCreator) comp).toData();
		this.editor1 = new FRCardConstraintsEditor(layout);
		this.renderer = new DefaultTableCellRenderer();
	}

	@Override
	public String getGroupName() {
		return Inter.getLocText("FR-Designer_Layout_Constraints");
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public TableCellRenderer getRenderer(int row) {
		return this.renderer;
	}

	@Override
	public TableCellEditor getEditor(int row) {
		return this.editor1;
	}

	@Override
	public Object getValue(int row, int column) {
		if (column == 0) {
			return Inter.getLocText("FR-Designer_Layout-Index");
		} else {
			return layout.getWidgetIndex(widget) + 1;
		}
	}

	@Override
	public boolean setValue(Object value, int row, int column) {
		if (column == 1) {
			layout.setWidgetIndex(widget, value == null ? 0 : (((Number) value).intValue() - 1));
			container.convert();
			//TODO:convert后丢失焦点
			return true;
		}
		return true;
	}

	/**
	 * 是否可编辑
	 * 
	 * @param row 当前行
	 * 
	 * @return 是否可编辑
	 * 
	 *
	 * @date 2014-12-30-下午5:14:31
	 * 
	 */
	public boolean isEditable(int row) {
		return true;
	}
}