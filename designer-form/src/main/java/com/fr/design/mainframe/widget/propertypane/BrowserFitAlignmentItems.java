package com.fr.design.mainframe.widget.propertypane;

import com.fr.conf.FitAttrState;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;

public class BrowserFitAlignmentItems implements ItemProvider {

    private static Item[] VALUE_ITEMS = {
            FitAttrState.HORIZONTAL_FIT.propertyItem(),
            FitAttrState.DOUBLE_FIT.propertyItem(),
            FitAttrState.NOT_FIT.propertyItem(),
    };

    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }


}
