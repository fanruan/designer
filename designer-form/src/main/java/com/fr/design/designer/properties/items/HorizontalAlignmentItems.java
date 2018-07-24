package com.fr.design.designer.properties.items;

import java.awt.FlowLayout;



public class HorizontalAlignmentItems implements ItemProvider {

    private static Item[] VALUE_ITEMS = {
        new Item(com.fr.design.i18n.Toolkit.i18nText("Left"), FlowLayout.LEFT),
        new Item(com.fr.design.i18n.Toolkit.i18nText("Right"), FlowLayout.RIGHT),
        new Item(com.fr.design.i18n.Toolkit.i18nText("Center"), FlowLayout.CENTER)
        };
        
    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
}