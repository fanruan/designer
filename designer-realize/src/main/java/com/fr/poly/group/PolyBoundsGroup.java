package com.fr.poly.group;

import java.awt.Rectangle;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.base.ScreenResolution;
import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.general.Inter;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.unit.UnitRectangle;

public class PolyBoundsGroup implements GroupModel {
	private static final int DEFAULT_ROW_COUNT = 4;
	
	private int resolution = ScreenResolution.getScreenResolution();
	private DefaultTableCellRenderer renderer;
	private TemplateBlock block;
	private PropertyCellEditor editor;
	private PolyWorkSheet worksheet;

	public PolyBoundsGroup(TemplateBlock block, PolyWorkSheet worksheet) {
		this.block = block;
		this.worksheet = worksheet;
		renderer = new DefaultTableCellRenderer();
		editor = new PropertyCellEditor(new IntegerPropertyEditor());
	}

	@Override
	public String getGroupName() {
		return Inter.getLocText("Form-Component_Bounds");
	}

	@Override
	public int getRowCount() {
		return DEFAULT_ROW_COUNT;
	}

	@Override
	public TableCellRenderer getRenderer(int row) {
		return renderer;
	}

	@Override
	public TableCellEditor getEditor(int row) {
		return editor;
	}

	@Override
	public Object getValue(int row, int column) {
		UnitRectangle ur = block.getBounds();
		Rectangle r = ur.toRectangle(resolution);
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
			switch (row) {
			case 0:
				return r.x;
			case 1:
				return r.y;
			case 2:
				return r.width;
			default:
				return r.height;
			}
		}
	}

	@Override
	public boolean setValue(Object value, int row, int column) {
		if (column == 1) {
			int v = value == null ? 0 : ((Number) value).intValue();
			UnitRectangle ur = block.getBounds();
			Rectangle r = ur.toRectangle(resolution);
			switch (row) {
			case 0:
				r.x = v;
				break;
			case 1:
				r.y = v;
				break;
			case 2:
				r.width = v;
				break;
			case 3:
				r.height = v;
				break;
			}

			UnitRectangle bounds = new UnitRectangle(new Rectangle(r.x, r.y, r.width, r.height), resolution);
			block.setBounds(bounds, worksheet);
			if (block instanceof com.fr.report.poly.AbstractPolyECBlock) {
				((com.fr.report.poly.AbstractPolyECBlock) block).reCalculateBlockSize();
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否可编辑
	 * 
	 * @param row 指定行
	 * 
	 * @return 是否可编辑
	 * 
	 */
	public boolean isEditable(int row) {
		return true;
	}
}