package com.fr.design.gui.icombobox.filter;

/**
 * Filter used in ComboBox.
 */
public class StartsWithFilter implements Filter {
    public boolean accept(String prefix, Object object) {
        return prefix == null || (object != null && object.toString() != null &&
                object.toString().toLowerCase().startsWith(prefix.toLowerCase()));
    }
}