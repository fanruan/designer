package com.fr.design.designer.properties.items;

import com.fr.general.Inter;

public class UnderlineItems implements ItemProvider {

    private static Item[] VALUE_ITEMS = {
        new Item(Inter.getLocText("DataFunction-None"), ""),
        new Item(Inter.getLocText("StyleAlignment-Top"), "overline"),
        new Item(Inter.getLocText("Center"), "line-through"),
        new Item(Inter.getLocText("StyleAlignment-Bottom"), "underline")};

    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
}