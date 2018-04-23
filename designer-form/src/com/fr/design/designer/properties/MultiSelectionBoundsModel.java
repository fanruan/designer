package com.fr.design.designer.properties;

import java.awt.Rectangle;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.general.Inter;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.designer.beans.ConstraintsGroupModel;

/**
 * 多个组件时候的属性组
 *
 */
public class MultiSelectionBoundsModel implements ConstraintsGroupModel {
	
	private DefaultTableCellRenderer renderer;
	private PropertyCellEditor editor;
	private FormDesigner designer;
	
	public MultiSelectionBoundsModel(FormDesigner designer) {
		this.designer = designer;
		renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
	}

	@Override
	public TableCellEditor getEditor(int row) {
		return editor;
	}

	@Override
	public String getGroupName() {
		return Inter.getLocText("Form-Component_Bounds");
	}

	@Override
	public TableCellRenderer getRenderer(int row) {
		return renderer;
	}

	@Override
	public int getRowCount() {
		return 4;
	}

	@Override
	public Object getValue(int row, int column) {
		if (column == 0) {
			switch (row) {
			case 0:
				return Inter.getLocText("X-Coordinate");
			case 1:
				return Inter.getLocText("Y-Coordinate");
			case 2:
				return Inter.getLocText("Tree-Width");
			default:
				return Inter.getLocText("Tree-Height");
			}
		} else {
			Rectangle rec = designer.getSelectionModel().getSelection().getSelctionBounds();
			switch (row) {
			case 0:
				return rec.x;
			case 1:
				return rec.y;
			case 2:
				return rec.width;
			default:
				return rec.height;
			}
		}
	}

	@Override
	public boolean isEditable(int row) {
		return true;
	}

	@Override
	public boolean setValue(Object value, int row, int column) {
		if (column == 1) {
			FormSelection selection = designer.getSelectionModel().getSelection();
			int v = value == null ? 0 : ((Number) value).intValue();
			Rectangle bounds = selection.getSelctionBounds();
			switch (row) {
			case 0:
				if (bounds.x == v)
					return false;
				bounds.x = v;
				break;
			case 1:
				if (bounds.y == v)
					return false;
				bounds.y = v;
				break;
			case 2:
				if (bounds.width == v)
					return false;
				bounds.width = v;
				break;
			case 3:
				if (bounds.height == v)
					return false;
				bounds.height = v;
				break;
			}
			selection.backupBounds();
			selection.setSelectionBounds(bounds, designer);
			selection.fixCreator(designer);
			return true;
		} else {
			return false;
		}
	}
}