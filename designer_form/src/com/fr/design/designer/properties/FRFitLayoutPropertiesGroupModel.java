/**
 * 
 */
package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.mainframe.widget.editors.BooleanEditor;
import com.fr.design.mainframe.widget.editors.FitLayoutDirectionEditor;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * 自适应布局自身的属性表
 * 主要为布局内组件间隔（合并水平和竖直间隔），原样缩放（为web端保持当前设计的款高比例）
 * 
 * @author jim
 * @date 2014-6-24
 */
public class FRFitLayoutPropertiesGroupModel implements GroupModel {
	
	private PropertyCellEditor editor;
	private DefaultTableCellRenderer renderer;
	private FitLayoutDirectionEditor stateEditor;
	private FitStateRenderer stateRenderer;
	private PropertyCellEditor reLayoutEditor;
	private CheckBoxCellRenderer reLayoutrenderer;
	private WFitLayout layout;
	private XWFitLayout xfl;
	
	public FRFitLayoutPropertiesGroupModel(XWFitLayout xfl){
		this.xfl = xfl;
		this.layout = xfl.toData();
		renderer = new DefaultTableCellRenderer();
		editor = new PropertyCellEditor(new IntegerPropertyEditor());
	    stateEditor = new FitLayoutDirectionEditor();
	    stateRenderer = new FitStateRenderer();
		reLayoutrenderer = new CheckBoxCellRenderer();
		reLayoutEditor = new PropertyCellEditor(new BooleanEditor());
	}

	/** 
	 * 布局管理器自己的属性
	 */
	@Override
	public String getGroupName() {
		return Inter.getLocText("FR-Designer-Layout_Adaptive_Layout");
	}

	@Override
	public int getRowCount() {
		return 3;
	}

	@Override
	public TableCellRenderer getRenderer(int row) {
		switch (row) {
	        case 0:
	            return renderer;
	        case 1:
	            return stateRenderer;
			default:
				return reLayoutrenderer;

		}
	}

	@Override
	public TableCellEditor getEditor(int row) {
		switch (row) {
	        case 0:
	            return editor;
	        case 1:
	            return stateEditor;
			default:
				return reLayoutEditor;
		}
	}

	@Override
	public Object getValue(int row, int column) {
		if (column == 0) {
            switch (row) {
                case 0:
                    return Inter.getLocText("FR-Designer_Component_Interval");
                case 1 :
                    return Inter.getLocText("FR-Designer_Component_Scale");
				default:
					return Inter.getLocText("FR-Designer-App_ReLayout");
            }
        } else {
            switch (row) {
                case 0:
                    return layout.getCompInterval();
                case 1 :
                	return layout.getCompState();
				default:
					return layout.getAppRelayout();
            }
        }
	}

	@Override
	public boolean setValue(Object value, int row, int column) {
		int state = 0;
		boolean appRelayoutState = true;
		if(value instanceof Integer) {
			state = (Integer)value;
		} else if (value instanceof Boolean) {
			appRelayoutState = (boolean) value;
		}
		if (column == 0 || state < 0) {
			return false;
		} else {
			if (row ==0 && xfl.canAddInterval(state)) {
				// 设置完间隔后，要同步处理界面组件，容器刷新后显示出对应效果
				setLayoutGap(state);
				return true;
			}else if (row == 1) {
				layout.setCompState(state);
				return true;
			} else if (row == 2) {
				layout.setAppRelayout(appRelayoutState);
				return true;
			} 
			return false;
		}
	}
	
	private void setLayoutGap(int value) {
		int  interval = layout.getCompInterval();
    	if (value != interval) {
    		xfl.moveContainerMargin();
    		xfl.moveCompInterval(xfl.getAcualInterval());
    		layout.setCompInterval(value);
    		xfl.addCompInterval(xfl.getAcualInterval());
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