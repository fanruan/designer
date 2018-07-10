package com.fr.design.designer.properties;

import com.fr.design.designer.properties.items.LayoutIndexItems;
import com.fr.form.ui.container.WCardLayout;

public class CardDefaultShowEditor extends EnumerationEditor {

    public CardDefaultShowEditor(WCardLayout layout) {
        super(new LayoutIndexItems(layout, false));
    }
}