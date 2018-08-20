/**
 * 
 */
package com.fr.design.designer.properties.items;

import com.fr.form.ui.container.WFitLayout;


/**
 * @author jim
 * @date 2014-7-31
 */
public class FRFitConstraintsItems implements ItemProvider{

	public static final Item[] ITEMS = new Item[] {
		new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Attr_Bidirectional_Adaptive"), WFitLayout.STATE_FULL),
		new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Attr_Horizontal_Adaptive"), WFitLayout.STATE_ORIGIN)};
	
	public Item[] getItems() {
		return ITEMS;
	}

}
