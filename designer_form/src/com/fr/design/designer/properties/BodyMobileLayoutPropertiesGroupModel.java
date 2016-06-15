package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.form.ui.container.WFitLayout;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.mainframe.widget.editors.BooleanEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class BodyMobileLayoutPropertiesGroupModel implements GroupModel {
	private PropertyCellEditor reLayoutEditor;
	private CheckBoxCellRenderer reLayoutrenderer;
	private WFitLayout layout;
	private XWFitLayout xfl;

	public BodyMobileLayoutPropertiesGroupModel(XWFitLayout xfl) {
		this.xfl = xfl;
		this.layout = xfl.toData();
		reLayoutrenderer = new CheckBoxCellRenderer();
		reLayoutEditor = new PropertyCellEditor(new BooleanEditor());
	}

	@Override
	public String getGroupName() {
		return Inter.getLocText("FR-Designer-Layout_Adaptive_Layout");
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public TableCellRenderer getRenderer(int row) {
		return reLayoutrenderer;
	}

	@Override
	public TableCellEditor getEditor(int row) {
		return reLayoutEditor;
	}

	@Override
	public Object getValue(int row, int column) {
		if (column == 0) {
			return Inter.getLocText("FR-Designer-App_ReLayout");
		}else {
			return layout.getAppRelayout();
		}
	}

	@Override
	public boolean setValue(Object value, int row, int column) {
		int state = 0;
		boolean appRelayoutState = true;
		if(value instanceof Integer) {
			state = (Integer)value;
		}else if (value instanceof Boolean) {
			appRelayoutState = (boolean)value;
		}
		if (column == 0 || state < 0) {
			return false;
		} else {
			layout.setAppRelayout(appRelayoutState);
			return true;
		}
	}

	/**
	 * 是否可编辑
	 * @param row 行
	 * @return 否
	 */
	@Override
	public boolean isEditable(int row) {
		return true;
	}

	private class CheckBoxCellRenderer extends UICheckBox implements TableCellRenderer {


		public CheckBoxCellRenderer() {
			super();
			setOpaque(true);

		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (value instanceof Boolean) {
				setSelected(((Boolean) value).booleanValue());
				setEnabled(table.isCellEditable(row, column));
				if (isSelected) {
					setBackground(table.getSelectionBackground());
					setForeground(table.getSelectionForeground());
				} else {
					setForeground(table.getForeground());
					setBackground(table.getBackground());
				}
			} else {
				return null;
			}
			return this;
		}
	}
}