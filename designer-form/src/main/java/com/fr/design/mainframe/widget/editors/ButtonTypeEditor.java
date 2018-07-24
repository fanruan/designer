package com.fr.design.mainframe.widget.editors;

import com.fr.design.designer.properties.items.Item;

import com.fr.stable.StringUtils;

public class ButtonTypeEditor extends ComboEditor {

	public ButtonTypeEditor() {
		super(new Item[] { new Item(com.fr.design.i18n.Toolkit.i18nText("Default"), false), new Item(com.fr.design.i18n.Toolkit.i18nText("Custom"), true), });
	}

	@Override
	public void setValue(Object value) {
		Item item = new Item(StringUtils.EMPTY, value);
		comboBox.setSelectedItem(item);
	}
	
	@Override
	public boolean refreshInTime(){
		return true;
	}

	@Override
	public Object getValue() {
		Item item = (Item) comboBox.getSelectedItem();
		return item.getValue();
	}
}