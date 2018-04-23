/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.xtable;

import java.beans.PropertyEditor;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.base.FRContext;
import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.mainframe.widget.renderer.PropertyCellRenderer;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class AbstractPropertyGroupModel implements GroupModel, Comparable<AbstractPropertyGroupModel> {
	
    protected String groupName;
    protected XCreator creator;
    protected CRPropertyDescriptor[] properties;
    protected TableCellRenderer[] renderers;
    protected PropertyCellEditor[] editors;
	public static final String RENDERER = "renderer";

    public AbstractPropertyGroupModel(String groupName, XCreator creator, CRPropertyDescriptor[] props) {
        this.groupName = groupName;
        this.creator = creator;
        this.properties = props;
        this.renderers = new TableCellRenderer[properties.length];
        this.editors = new PropertyCellEditor[properties.length];
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public int getRowCount() {
        return properties.length;
    }

    @Override
    public TableCellRenderer getRenderer(int row) {
        if (renderers[row] == null) {
            try {
                initRenderer(row);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return renderers[row];
    }

    @Override
    public TableCellEditor getEditor(int row) {
        if (editors[row] == null) {
            try {
                initEditor(row);
            } catch (Exception ex) {
                FRContext.getLogger().error(ex.getMessage(), ex);
            }
        }
        return editors[row];
    }

    private void initRenderer(int row) throws Exception {
        Class rendererCls = (Class) properties[row].getValue(RENDERER);
        // 先看看有没有设定好的渲染器
        if (rendererCls != null) {
            renderers[row] = (TableCellRenderer) rendererCls.newInstance();
        } else {
            // 没有设定好的渲染器那么就根据已有的类型查找一个合适的渲染器
            Class propType = properties[row].getPropertyType();
            Class<? extends TableCellRenderer> rendererClass = TableUtils.getTableCellRendererClass(propType);
            if (rendererClass != null) {
                renderers[row] = rendererClass.newInstance();
            } else {
                // 如果已有的渲染器也不匹配那么就根据编辑器来生成一个渲染器
                Class<?> editorClass =  properties[row].getPropertyEditorClass();
                if (editorClass == null) {
                    editorClass = TableUtils.getPropertyEditorClass(propType);
                }
                if (editorClass == null) {
                    renderers[row] = new DefaultTableCellRenderer();
                } else {
                    PropertyEditor editor = ((PropertyCellEditor)getEditor(row)).getCellEditor();
                    renderers[row] = new PropertyCellRenderer(editor);
                }
            }
        }
    }
    
    protected abstract void initEditor(final int row) throws Exception;
}