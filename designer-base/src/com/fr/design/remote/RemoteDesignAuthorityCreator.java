package com.fr.design.remote;

import com.fr.design.beans.BasicBeanPane;
import com.fr.report.DesignAuthority;

import javax.swing.Icon;

public class RemoteDesignAuthorityCreator {

    private String name;
    private Icon icon;
    private Class clazz;
    private Class<? extends BasicBeanPane> editorClazz;


    public RemoteDesignAuthorityCreator(String name, Icon icon, Class clazz, Class<? extends BasicBeanPane> editorClazz) {
        this.name = name;
        this.icon = icon;
        this.clazz = clazz;
        this.editorClazz = editorClazz;
    }

    public boolean accept(Object object) {
        return this.clazz != null && this.clazz.isInstance(object);
    }

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }

    public Class getClazz() {
        return clazz;
    }

    public Class<? extends BasicBeanPane> getEditorClazz() {
        return editorClazz;
    }

    public void saveUpdatedBean(DesignAuthority authority, Object bean) {
        authority.setItems(((DesignAuthority) bean).getItems());
    }
}
