package com.fr.design.designer.properties.items;


import com.fr.stable.ArrayUtils;
import com.fr.form.ui.container.WBorderLayout;

public class FRBorderConstraintsItems implements ItemProvider {

    private Item[] VALUE_ITEMS;

	public FRBorderConstraintsItems(String[] directions) {
		Item[] item = createItems(directions);
		VALUE_ITEMS = (Item[]) ArrayUtils.add(item, new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_BorderLayout_Center"),
				WBorderLayout.CENTER));
	}
	
	@Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
	
	public static Item[] createItems(String[] directions) {
		Item[] items = new Item[directions.length];
		for (int i = 0; i < directions.length; i++) {
			if (WBorderLayout.NORTH.equals(directions[i])) {
				items[i] = new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_BorderLayout_North"), WBorderLayout.NORTH);
			} else if (WBorderLayout.SOUTH.equals(directions[i])) {
				items[i] = new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_BorderLayout_South"), WBorderLayout.SOUTH);
			} else if (WBorderLayout.WEST.equals(directions[i])) {
				items[i] = new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_BorderLayout_West"), WBorderLayout.WEST);
			} else if (WBorderLayout.EAST.equals(directions[i])) {
				items[i] = new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_BorderLayout_East"), WBorderLayout.EAST);
			}
		}
		return items;
	}
}