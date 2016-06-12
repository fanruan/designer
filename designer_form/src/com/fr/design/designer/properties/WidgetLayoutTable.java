package com.fr.design.designer.properties;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.*;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.gui.itable.PropertyGroup;
import com.fr.design.designer.beans.LayoutAdapter;


public class WidgetLayoutTable extends AbstractPropertyTable {

	private XWBodyFitLayout xwBodyFitLayout;

	public WidgetLayoutTable(XWBodyFitLayout xwBodyFitLayout) {
		super();
		setDesigner(xwBodyFitLayout);
	}
	
	public static ArrayList<PropertyGroup> getCreatorPropertyGroup(XCreator source) {
		ArrayList<PropertyGroup> groups = new ArrayList<PropertyGroup>();
		if (source instanceof XLayoutContainer) {
			LayoutAdapter layoutAdapter = ((XLayoutContainer)source).getLayoutAdapter();
			if(layoutAdapter != null){
				GroupModel m = layoutAdapter.getLayoutProperties();
				if (m != null) {
					groups.add(new PropertyGroup(m));
				}
			}
		}
		return groups;
	}

    /**
     * 初始化属性表组
     * @param source    控件
     */
	public void initPropertyGroups(Object source) {

		groups = getCreatorPropertyGroup(xwBodyFitLayout);

		TableModel model = new BeanTableModel();
		setModel(model);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumn tc = this.getColumn(this.getColumnName(0));
		tc.setPreferredWidth(30);
		this.repaint();
	}

	private void setDesigner(XWBodyFitLayout xwBodyFitLayout) {
		this.xwBodyFitLayout = xwBodyFitLayout;
	}


	/**
	 * 单元格tooltip
	 * 属性名悬浮提示 
	 * 
	 * @param 鼠标点击事件
	 * @return 单元格tooltip
	 */
	public String getToolTipText(MouseEvent event) {
		int row = WidgetLayoutTable.super.rowAtPoint(event.getPoint());
		int column = WidgetLayoutTable.super.columnAtPoint(event.getPoint());
		if(row != -1 && column == 0){
			return String.valueOf(this.getValueAt(row, column));
		}
		return null;
	}
	
    /**
     * 待说明
     */
	public void firePropertyEdit() {

	}
}