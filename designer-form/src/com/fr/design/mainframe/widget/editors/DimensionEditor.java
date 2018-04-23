package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleDimensionEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

public class DimensionEditor extends AccessiblePropertyEditor {

    public DimensionEditor() {
        super(new AccessibleDimensionEditor());
    }
}