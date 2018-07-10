package com.fr.design.designer.properties;

import com.fr.design.designer.properties.items.FRBorderConstraintsItems;

public class FRBorderConstraintsEditor extends EnumerationEditor {

    public FRBorderConstraintsEditor(String[] directions) {
        super(new FRBorderConstraintsItems(directions));
    }
}