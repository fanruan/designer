package com.fr.design.designer.properties;

import com.fr.design.mainframe.widget.accessibles.AccessibleIconEditor;

public class IconCellEditor extends DelegateEditor {

    public IconCellEditor() {
        final AccessibleIconEditor iconEditor = new AccessibleIconEditor();
        editorComponent = iconEditor;
        delegate = new EditorDelegate() {

            @Override
            public void setValue(Object value) {
                iconEditor.setValue(value);
            }

            @Override
            public Object getCellEditorValue() {
                return iconEditor.getValue();
            }
        };
        iconEditor.addChangeListener(delegate);
    }
}