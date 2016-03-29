package com.fr.design.designer.properties.items;

import java.awt.FlowLayout;

import com.fr.general.Inter;

public class HorizontalAlignmentItems implements ItemProvider {

    private static Item[] VALUE_ITEMS = {
        new Item(Inter.getLocText("Left"), FlowLayout.LEFT),
        new Item(Inter.getLocText("Right"), FlowLayout.RIGHT),
        new Item(Inter.getLocText("Center"), FlowLayout.CENTER)
        };
        
    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
}