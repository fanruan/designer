package com.fr.design.mainframe.widget.editors;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public abstract class AbstractPropertyEditor implements ExtendedPropertyEditor {

    private ArrayList<PropertyChangeListener> listeners;

    public AbstractPropertyEditor() {
        listeners = new ArrayList<PropertyChangeListener>();
    }

    @Override
    public boolean isPaintable() {
        return false;
    }

    @Override
    public void paintValue(Graphics gfx, Rectangle box) {
    }

    @Override
    public String getJavaInitializationString() {
        return null;
    }

    @Override
    public String getAsText() {
        return null;
    }
    
    @Override
    public boolean refreshInTime() {
    	return false;
    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
    }

    @Override
    public String[] getTags() {
        return new String[0];
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    protected void firePropertyChanged() {
        PropertyChangeEvent evt = new PropertyChangeEvent(this, null, null, null);

        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(evt);
        }
    }

    @Override
    public void setDefaultValue(Object v) {
    }
}