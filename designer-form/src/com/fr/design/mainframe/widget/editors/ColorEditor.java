package com.fr.design.mainframe.widget.editors;

import java.awt.Color;

import com.fr.design.mainframe.widget.accessibles.AccessibleColorEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

public class ColorEditor extends AccessiblePropertyEditor {

    public ColorEditor() {
        super(new AccessibleColorEditor());
    }

    @Override
    public void setDefaultValue(Object v) {
        ((AccessibleColorEditor) editor).setDefaultColor((Color) v);
    }
}