package com.fr.design.designer.properties.items;

import com.fr.form.ui.container.WBodyLayoutType;

/**
 * Created by zhouping on 2016/9/18.
 */
public class FRLayoutTypeItems implements ItemProvider{
    public static final Item[] ITEMS = new Item[] {
            new Item(WBodyLayoutType.FIT.description(), WBodyLayoutType.FIT.getTypeValue()),
            new Item(WBodyLayoutType.ABSOLUTE.description(), WBodyLayoutType.ABSOLUTE.getTypeValue())};

    public Item[] getItems() {
        return ITEMS;
    }
}
