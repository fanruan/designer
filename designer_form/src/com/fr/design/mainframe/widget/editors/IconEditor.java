package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleIconEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

public class IconEditor extends AccessiblePropertyEditor {

    public IconEditor() {
        super(new AccessibleIconEditor());
    }
}