package com.fr.design.designer.properties;

import com.fr.design.designer.properties.items.LayoutIndexItems;
import com.fr.form.ui.container.WLayout;

public class FRCardConstraintsEditor extends EnumerationEditor {
	public FRCardConstraintsEditor(WLayout layout) {
        super(new LayoutIndexItems(layout, true));
    }
}