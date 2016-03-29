/**
 * 
 */
package com.fr.design.designer.properties.items;

import com.fr.form.ui.container.WFitLayout;
import com.fr.general.Inter;

/**
 * @author jim
 * @date 2014-7-31
 */
public class FRFitConstraintsItems implements ItemProvider{

	public static final Item[] ITEMS = new Item[] {
		new Item(Inter.getLocText("Adaptive_Full_Area"), WFitLayout.STATE_FULL),
		new Item(Inter.getLocText("Adaptive_Original_Scale"), WFitLayout.STATE_ORIGIN)};
	
	public Item[] getItems() {
		return ITEMS;
	}

}