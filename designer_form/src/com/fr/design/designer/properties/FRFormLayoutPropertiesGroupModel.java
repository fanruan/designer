/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.properties;

import java.awt.Component;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.general.Inter;
import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;

/**
 * @author richer
 * @since 6.5.3
 * 表单布局也需要边界属性
 */
public class FRFormLayoutPropertiesGroupModel implements GroupModel{
    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    
    public FRFormLayoutPropertiesGroupModel(Component comp) {
        this.renderer = new DefaultTableCellRenderer();
        this.editor = new PropertyCellEditor(new IntegerPropertyEditor());
    }
    
    @Override
    public String getGroupName() {
        return Inter.getLocText("Form-Component_Bounds");
    }

    @Override
    public int getRowCount() {
        return 4;
    }

    @Override
    public TableCellRenderer getRenderer(int row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TableCellEditor getEditor(int row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getValue(int row, int column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setValue(Object value, int row, int column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEditable(int row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}