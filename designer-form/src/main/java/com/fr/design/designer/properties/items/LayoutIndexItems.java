/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.properties.items;

import com.fr.base.Utils;
import com.fr.form.ui.container.WLayout;

/**
 * @author richer
 * @since 6.5.3
 */
public class LayoutIndexItems implements ItemProvider {

    private WLayout layout;
    private boolean chooseIndexNotName;

    public LayoutIndexItems(WLayout layout, boolean chooseIndexNotName) {
        this.layout = layout;
        this.chooseIndexNotName = chooseIndexNotName;
    }

    @Override
    public Item[] getItems() {
        int count = layout.getWidgetCount();
        Item[] items = new Item[count];
        for (int i = 0; i < count; i ++) {
        	if (chooseIndexNotName) {
        		items[i] = new Item(Utils.doubleToString(i + 1), i + 1);
        	} else {
        		items[i] = new Item(layout.getWidget(i).getWidgetName(), i);
        	}
        }
        return items;
    }
}