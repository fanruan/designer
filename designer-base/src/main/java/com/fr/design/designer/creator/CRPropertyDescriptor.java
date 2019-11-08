/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeListener;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

/**
 * @author richer
 * @since 6.5.3
 */
public final class CRPropertyDescriptor extends PropertyDescriptor {
    public static final String RENDERER = "renderer";
    private boolean isSubProperty = false;

    private PropertyChangeListener l;

    public CRPropertyDescriptor(String name, Class<?> beanClass) throws IntrospectionException {
        super(name, beanClass);
    }

    public CRPropertyDescriptor(String name, Class<?> beanClass, String readMethod, String writeMethod) throws IntrospectionException {
        super(name, beanClass, readMethod, writeMethod);
    }


    public CRPropertyDescriptor putKeyValue(String key, Object value) {
        if (StringUtils.isNotEmpty(key)) {
            this.setValue(key, value);
        }
        return this;
    }

    public CRPropertyDescriptor setPropertyChangeListener(PropertyChangeListener l) {
        this.l = l;
        return this;
    }

    public void firePropertyChanged() {
        if (l != null) {
            l.propertyChange();
        }
    }

    public CRPropertyDescriptor setEditorClass(Class<?> clazz) {
        this.setPropertyEditorClass(clazz);
        return this;
    }

    public CRPropertyDescriptor setRendererClass(Class<?> clazz) {
        this.putKeyValue(RENDERER, clazz);
        return this;
    }

    public CRPropertyDescriptor setI18NName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public CRPropertyDescriptor setSubLevel(boolean isSubProperty) {
        this.isSubProperty = isSubProperty;
        return this;
    }

    public boolean isSubLevel() {
        return isSubProperty;
    }
}