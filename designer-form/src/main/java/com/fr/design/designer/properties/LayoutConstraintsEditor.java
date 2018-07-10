/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.properties;

import com.fr.design.designer.properties.items.LayoutIndexItems;
import com.fr.form.ui.container.WLayout;

/**
 * @author richer
 * @since 6.5.3
 */
public class LayoutConstraintsEditor extends EnumerationEditor {
    public LayoutConstraintsEditor(WLayout layout) {
        super(new LayoutIndexItems(layout, true));
    }
}