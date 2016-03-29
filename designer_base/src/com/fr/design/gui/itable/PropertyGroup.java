package com.fr.design.gui.itable;

import javax.swing.table.TableCellRenderer;

import com.fr.design.beans.GroupModel;

public class PropertyGroup {

    private GroupModel model;
    private String name;
    private boolean collapsed;
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