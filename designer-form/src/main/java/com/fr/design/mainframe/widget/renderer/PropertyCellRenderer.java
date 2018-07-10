package com.fr.design.mainframe.widget.renderer;


import java.awt.Component;
import java.beans.PropertyEditor;

public class PropertyCellRenderer extends GenericCellRenderer {

    private PropertyEditor editor;

    public PropertyCellRenderer(PropertyEditor editor) {
        this.editor = editor;
    }

    @Override
    public void setValue(Object value) {
        editor.setValue(value);
    }

    @Override
    public Component getRendererComponent() {
        return editor.getCustomEditor();
    }
}