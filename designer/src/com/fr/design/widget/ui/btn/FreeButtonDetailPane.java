package com.fr.design.widget.ui.btn;

import java.awt.Component;

import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;
import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;

public class FreeButtonDetailPane extends ButtonWithHotkeysDetailPane<FreeButton> {
	private com.fr.design.widget.ui.designer.btn.ButtonSytleDefinedPane stylePane;

	@Override
	protected Component createCenterPane() {
		return stylePane = new com.fr.design.widget.ui.designer.btn.ButtonSytleDefinedPane();
	}
	
	@Override
	public FreeButton createButton() {
		return new FreeButton();
	}

	@Override
	public void populate(Button button) {
		super.populate(button);
		if(button instanceof FreeButton) {
			stylePane.populate((FreeButton) button);
		}
	}

	@Override
	public FreeButton update() {
		FreeButton button = super.update();
		return stylePane.update(button);
	}

	@Override
	public Class classType() {
		return FreeButton.class;
	}
}