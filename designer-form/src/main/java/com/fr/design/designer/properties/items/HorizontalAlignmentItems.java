package com.fr.design.designer.properties.items;

import java.awt.FlowLayout;



public class HorizontalAlignmentItems implements ItemProvider {

    private static Item[] VALUE_ITEMS = {
        new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Left"), FlowLayout.LEFT),
        new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Right"), FlowLayout.RIGHT),
        new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Center"), FlowLayout.CENTER)
        };
        
    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
}