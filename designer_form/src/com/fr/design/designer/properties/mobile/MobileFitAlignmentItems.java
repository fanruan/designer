package com.fr.design.designer.properties.mobile;

import com.fr.base.mobile.MobileFitAttrState;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;

public class MobileFitAlignmentItems implements ItemProvider{

    private static Item[] VALUE_ITEMS;

    static {
        MobileFitAttrState[] allStates = MobileFitAttrState.values();
        int len = allStates.length;
        VALUE_ITEMS = new Item[len];
        for (int i = 0; i < len ; i++) {
            VALUE_ITEMS[i] = new Item(allStates[i].description(), allStates[i]);
        }
    }

    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }

}
