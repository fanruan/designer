package com.fr.design.gui.imenu;

import com.fr.design.constants.UIConstants;

public class UICheckBoxMenuItem extends UIMenuItem {

	public UICheckBoxMenuItem(String name) {
		super(name);
	}


	public void setState(boolean b) {
		synchronized (this) {
			setSelected(b);
		}
	}

	@Override
	public void setSelected(boolean b) {
		super.setSelected(b);
		if (this.isSelected()) {
			setIcon(UIConstants.CHOOSEN_ICON);
		} else {
			setIcon(UIConstants.BLACK_ICON);
		}
	}


}