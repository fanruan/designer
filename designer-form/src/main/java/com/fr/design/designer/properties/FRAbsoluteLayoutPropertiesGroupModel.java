package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.mainframe.widget.editors.AbsoluteLayoutDirectionEditor;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WAbsoluteLayout;


import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Created by zhouping on 2016/8/1.
 */
public class FRAbsoluteLayoutPropertiesGroupModel implements GroupModel {

    private PropertyCellEditor editor;
    private DefaultTableCellRenderer renderer;
    protected AbsoluteLayoutDirectionEditor stateEditor;
    protected AbsoluteStateRenderer stateRenderer;
    protected WAbsoluteLayout layout;
    protected XWAbsoluteLayout xwAbsoluteLayout;

    public FRAbsoluteLayoutPropertiesGroupModel(XWAbsoluteLayout xwAbsoluteLayout){
        this.xwAbsoluteLayout = xwAbsoluteLayout;
        this.layout = xwAbsoluteLayout.toData();
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
        stateEditor = new AbsoluteLayoutDirectionEditor();
        stateRenderer = new AbsoluteStateRenderer();
    }

    /**
     * 布局管理器自己的属性
     */
    @Override
    public String getGroupName() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Area_Scaling");
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public TableCellRenderer getRenderer(int row) {
        return stateRenderer;
    }

    @Override
    public TableCellEditor getEditor(int row) {
        return stateEditor;
    }

    @Override
    public Object getValue(int row, int column) {
        if (column == 0) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Scaling_Mode");
        } else {
            return layout.getCompState();
        }
    }

    @Override
    public boolean setValue(Object value, int row, int column) {
        int state = 0;
        if(value instanceof Integer) {
            state = (Integer)value;
        }
        if (column == 0 || state < 0) {
            return false;
        } else {
            if (row == 0) {
                layout.setCompState(state);
                return true;
            }
            return false;
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
}
