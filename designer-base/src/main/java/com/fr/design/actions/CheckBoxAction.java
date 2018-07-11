/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions;

import javax.swing.JCheckBoxMenuItem;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UICheckBoxMenuItem;

/**
 * check box
 */
public abstract class CheckBoxAction extends UpdateAction {
	public UICheckBoxMenuItem createMenuItem() {
		Object object = this.getValue(UICheckBoxMenuItem.class.getName());
		if (object == null) {
			object = createCheckBoxMenuItem(this);
			this.putValue(UICheckBoxMenuItem.class.getName(), object);
		}

		// isSelected.
		((JCheckBoxMenuItem) object).setSelected(this.isSelected());

		return (UICheckBoxMenuItem) object;
	}
	
	

	public abstract boolean isSelected();


	public boolean isRadioSelect() {
		return false;
	}

	public void setSelected(boolean isSelected) {
		UIButton button = (UIButton) this.createToolBarComponent();
		button.setSelected(isSelected);
	}
}