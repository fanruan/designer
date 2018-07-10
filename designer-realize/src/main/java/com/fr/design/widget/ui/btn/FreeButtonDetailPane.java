package com.fr.design.widget.ui.btn;

import java.awt.Component;
import com.fr.form.ui.FreeButton;
import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;


public class FreeButtonDetailPane extends ButtonWithHotkeysDetailPane<FreeButton> {
	private ButtonSytleDefinedPane buttonSytleDefinedPane;

	@Override
	protected Component createCenterPane() {
		buttonSytleDefinedPane = new ButtonSytleDefinedPane();
		return buttonSytleDefinedPane;
	}
	
	@Override
	public FreeButton createButton() {
		return new FreeButton();
	}

	public void populate(FreeButton button) {
		super.populate(button);
		buttonSytleDefinedPane.populate(button);
	}

	@Override
	public FreeButton update() {
		FreeButton button = super.update();
		buttonSytleDefinedPane.update(button);
		return button;
	}

	@Override
	public Class classType() {
		return FreeButton.class;
	}
}