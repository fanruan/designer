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
		new Item(Inter.getLocText("FR-Designer_Attr_Bidirectional_Adaptive"), WFitLayout.STATE_FULL),
		new Item(Inter.getLocText("FR_Designer_Attr_Horizontal_Adaptive"), WFitLayout.STATE_ORIGIN)};
	
	public Item[] getItems() {
		return ITEMS;
	}

}