package com.fr.design.designer.properties;

import java.awt.Rectangle;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.general.Inter;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.form.ui.container.WAbsoluteLayout;

/**
 * 绝对布局时候的属性组
 */
public class BoundsGroupModel implements ConstraintsGroupModel {
    private static final int MINHEIGHT = 21;
    private static final int FOUR = 4;

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private XCreator component;
    private XWAbsoluteLayout parent;

    public BoundsGroupModel(XWAbsoluteLayout parent, XCreator comp) {
        this.parent = parent;
        component = comp;
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    @Override
    public String getGroupName() {
        return Inter.getLocText("Form-Component_Bounds");
    }

    @Override
    public int getRowCount() {
        return FOUR;
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
        if (column == 0) {
            switch (row) {
                case 0:
                    return Inter.getLocText("FR-Designer_X_Coordinate");
                case 1:
                    return Inter.getLocText("FR-Designer_Y_Coordinate");
                case 2:
                    return Inter.getLocText("FR-Designer_Widget_Width");
                default:
                    return Inter.getLocText("FR-Designer_Widget_Height");
            }
        } else {
            switch (row) {
                case 0:
                    return component.getX();
                case 1:
                    return component.getY();
                case 2:
                    return component.getWidth();
                default:
                    return component.getHeight();
            }
        }
    }

    @Override
    public boolean setValue(Object value, int row, int column) {
        if (column == 1) {
        	int v = value == null ? 0 : ((Number) value).intValue();
            Rectangle bounds = new Rectangle(component.getBounds());
			switch (row) {
			case 0:
				if (bounds.x == v){
					return false;
                }
				bounds.x = v;
				break;
			case 1:
				if (bounds.y == v){
					return false;
                }
				bounds.y = v;
				break;
			case 2:
				if (bounds.width == v){
					return false;
                }
				bounds.width = v;
				break;
			case 3:
                if(v < MINHEIGHT){
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer_Min_Height") + "21");
                    v = component.getHeight();
                }
				if (bounds.height == v){
					return false;
                }
				bounds.height = v;
				break;
			}
            WAbsoluteLayout wabs = parent.toData();
            wabs.setBounds(component.toData(),bounds);
            component.setBounds(bounds);
            
            return true;
        } else {
            return false;
        }
    }

    /**
     * 属性组是否可编辑
     * @param row 第几行
     * @return 组件可编辑
     */
	@Override
	public boolean isEditable(int row) {
        //这里不需要为自定义按钮屏蔽大小属性
		return true;
	}
}