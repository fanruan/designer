package com.fr.design.designer.properties.items;



public class UnderlineItems implements ItemProvider {

    private static Item[] VALUE_ITEMS = {
        new Item(com.fr.design.i18n.Toolkit.i18nText("DataFunction-None"), ""),
        new Item(com.fr.design.i18n.Toolkit.i18nText("StyleAlignment-Top"), "overline"),
        new Item(com.fr.design.i18n.Toolkit.i18nText("Center"), "line-through"),
        new Item(com.fr.design.i18n.Toolkit.i18nText("StyleAlignment-Bottom"), "underline")};

    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
}