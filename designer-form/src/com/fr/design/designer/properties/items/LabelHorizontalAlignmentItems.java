package com.fr.design.designer.properties.items;

import javax.swing.SwingConstants;

import com.fr.general.Inter;

public class LabelHorizontalAlignmentItems implements ItemProvider {

    private static Item[] VALUE_ITEMS = {
        new Item(Inter.getLocText("Left"), SwingConstants.LEFT),
        new Item(Inter.getLocText("Right"), SwingConstants.RIGHT),
        new Item(Inter.getLocText("Center"), SwingConstants.CENTER)
        };
        
    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
}