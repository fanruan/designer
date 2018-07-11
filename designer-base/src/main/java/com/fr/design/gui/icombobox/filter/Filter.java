package com.fr.design.gui.icombobox.filter;

/**
 * Filter used in ComboBox.
 */
public interface Filter {
    public boolean accept(String prefix, Object object);
}