package com.fr.design.designer.properties.items;

import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.form.ui.container.WBorderLayout;

public class FRBorderConstraintsItems implements ItemProvider {

    private Item[] VALUE_ITEMS;

	public FRBorderConstraintsItems(String[] directions) {
		Item[] item = createItems(directions);
		VALUE_ITEMS = (Item[]) ArrayUtils.add(item, new Item(Inter.getLocText("BorderLayout-Center"),
				WBorderLayout.CENTER));
	}
	
	@Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
	
	public static Item[] createItems(String[] directions) {
		Item[] items = new Item[directions.length];
		for (int i = 0; i < directions.length; i++) {
			if (WBorderLayout.NORTH == directions[i]) {
				items[i] = new Item(Inter.getLocText("BorderLayout-North"), WBorderLayout.NORTH);
			} else if (WBorderLayout.SOUTH == directions[i]) {
				items[i] = new Item(Inter.getLocText("BorderLayout-South"), WBorderLayout.SOUTH);
			} else if (WBorderLayout.WEST == directions[i]) {
				items[i] = new Item(Inter.getLocText("BorderLayout-West"), WBorderLayout.WEST);
			} else if (WBorderLayout.EAST == directions[i]) {
				items[i] = new Item(Inter.getLocText("BorderLayout-East"), WBorderLayout.EAST);
			}
		}
		return items;
	}
}