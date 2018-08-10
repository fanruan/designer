package com.fr.design.designer.properties;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WAbsoluteLayout;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.Rectangle;

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
        return com.fr.design.i18n.Toolkit.i18nText("Form-Component_Bounds");
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
                    return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_X_Coordinate");
                case 1:
                    return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Y_Coordinate");
                case 2:
                    return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Width");
                default:
                    return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Height");
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
                    JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Min_Height") + "21");
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
