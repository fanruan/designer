package com.fr.design.designer.properties.items;



public class UnderlineItems implements ItemProvider {

    private static Item[] VALUE_ITEMS = {
        new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Function_None"), ""),
        new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Top"), "overline"),
        new Item(com.fr.design.i18n.Toolkit.i18nText("Center"), "line-through"),
        new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Bottom"), "underline")};

    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
}
