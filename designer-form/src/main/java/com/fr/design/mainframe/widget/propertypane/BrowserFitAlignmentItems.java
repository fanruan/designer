package com.fr.design.mainframe.widget.propertypane;

import com.fr.design.reportfit.FitType;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;

public class BrowserFitAlignmentItems implements ItemProvider {

    private static Item[] VALUE_ITEMS = {
            FitType.HORIZONTAL_FIT.propertyItem(),
            FitType.DOUBLE_FIT.propertyItem(),
            FitType.NOT_FIT.propertyItem(),
    };

    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }


}
