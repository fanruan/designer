package com.fr.design.designer.properties;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.fr.design.beans.GroupModel;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.gui.itable.PropertyGroup;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.general.ComparatorUtils;

public class WidgetPropertyTable extends AbstractPropertyTable {

	private FormDesigner designer;
	private static final int LEFT_COLUMN_WIDTH = 97;  // "属性名"列的宽度

	public WidgetPropertyTable(FormDesigner designer) {
		super();
		setDesigner(designer);
	}
	
	public static ArrayList<PropertyGroup> getCreatorPropertyGroup(FormDesigner designer, XCreator source) {
		ArrayList<PropertyGroup> groups = new ArrayList<PropertyGroup>();
		ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, source);
		ArrayList<GroupModel> groupModels = adapter.getXCreatorPropertyModel();
		for (GroupModel model : groupModels) {
			groups.add(new PropertyGroup(model));
		}
		XLayoutContainer container = XCreatorUtils.getParentXLayoutContainer(source);
		if (source.acceptType(XWFitLayout.class) || source.acceptType(XWParameterLayout.class)) {
			container = null;
		}
		if (container != null && !(source instanceof XWCardLayout)) {
			LayoutAdapter containerAdapter = container.getLayoutAdapter();
			GroupModel m = containerAdapter.getLayoutConstraints(source);
			if (m != null) {
				groups.add(new PropertyGroup(m));
			}
		}
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
		int size = designer.getSelectionModel().getSelection().size();
		if (size == 0 || size == 1) {
			XCreator creator = size == 0 ? designer.getRootComponent() : designer.getSelectionModel().getSelection()
					.getSelectedCreator();
			if (ComparatorUtils.equals(creator, designer.getRootComponent())) {
				groups = designer.getDesignerMode().createRootDesignerPropertyGroup();
			} else {
				groups = getCreatorPropertyGroup(designer, creator);
			}
		} else {
			groups = new ArrayList<PropertyGroup>();
			GroupModel multiModel = new MultiSelectionBoundsModel(designer);
			groups.add(new PropertyGroup(multiModel));
		}
		TableModel model = new BeanTableModel();
		setModel(model);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumn tc = this.getColumn(this.getColumnName(0));
		tc.setMinWidth(LEFT_COLUMN_WIDTH);
		tc.setMaxWidth(LEFT_COLUMN_WIDTH);

		this.repaint();
	}

	private void setRightColumnWidth(boolean automode) {
		int rightColumnWidth = this.getWidth() - LEFT_COLUMN_WIDTH;
		TableColumn tcRight = this.getColumn(this.getColumnName(1));
		tcRight.setMinWidth(automode ? 0 : rightColumnWidth);
		tcRight.setMaxWidth(automode ? this.getWidth() : rightColumnWidth);
	}
	
	private void setDesigner(FormDesigner designer) {
		this.designer = designer;
	}


	/**
	 * 单元格tooltip
	 * 属性名悬浮提示 
	 * 
	 * @param 鼠标点击事件
	 * @return 单元格tooltip
	 */
	public String getToolTipText(MouseEvent event) {
		int row = WidgetPropertyTable.super.rowAtPoint(event.getPoint());
		int column = WidgetPropertyTable.super.columnAtPoint(event.getPoint());
		if(row != -1 && column == 0){
			return String.valueOf(this.getValueAt(row, column));
		}
		return null;
	}

	@Override
	public void columnMarginChanged(javax.swing.event.ChangeEvent e) {
		setRightColumnWidth(false);
		super.columnMarginChanged(e);
		setRightColumnWidth(true);
	}

    /**
     * 待说明
     */
	public void firePropertyEdit() {
		designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
	}
}