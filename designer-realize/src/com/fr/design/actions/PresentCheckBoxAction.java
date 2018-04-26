package com.fr.design.actions;

import com.fr.design.gui.imenu.UICheckBoxMenuItem;
import com.fr.design.mainframe.ElementCasePane;

public abstract class PresentCheckBoxAction extends ElementCaseAction {
	protected PresentCheckBoxAction(ElementCasePane t) {
		super(t);
	}

	@Override
	public UICheckBoxMenuItem createMenuItem() {
		Object object = this.getValue(UICheckBoxMenuItem.class.getName());
		if (object == null) {
			object = createCheckBoxMenuItem(this);
			this.putValue(UICheckBoxMenuItem.class.getName(), object);
		}

		// isSelected.
		((UICheckBoxMenuItem) object).setSelected(this.isSelected());

		return (UICheckBoxMenuItem) object;
	}

	public abstract boolean isSelected();
}