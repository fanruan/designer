package com.fr.design.gui.itable;

import javax.swing.table.TableCellRenderer;

import com.fr.design.beans.GroupModel;

/**
 * PropertyGroup类描述了属性表中一个属性分组
 */
public class PropertyGroup {

    private GroupModel model; // 描述这个属性组中的数据model
    private String name; // 这组属性的标题名称
    private boolean collapsed; // 这组属性是否折叠
    private GroupRenderer renderer1;
    private GroupRenderer renderer2;

    public PropertyGroup(GroupModel model) {
        this(model.getGroupName(), model, false);
        renderer1 = new GroupRenderer();
        renderer2 = new GroupRenderer();
    }

    public PropertyGroup(String name, GroupModel model, boolean collapsed) {
        this.name = name;
        this.model = model;
        this.collapsed = collapsed;
    }

    public GroupModel getModel() {
        return model;
    }

    public void setModel(GroupModel model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public TableCellRenderer getFirstRenderer() {
        return renderer1;
    }

    public TableCellRenderer getSecondRenderer() {
        return renderer2;
    }

}