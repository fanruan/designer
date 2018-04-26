package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleParameterEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

public class ParameterEditor extends AccessiblePropertyEditor {

    public ParameterEditor() {
        super(new AccessibleParameterEditor());
    }
}