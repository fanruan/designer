package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleFontEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

public class FontEditor extends AccessiblePropertyEditor {

    public FontEditor() {
        super(new AccessibleFontEditor());
    }
}