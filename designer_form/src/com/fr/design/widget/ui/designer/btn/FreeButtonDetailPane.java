package com.fr.design.widget.ui.designer.btn;

import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;
import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;

import java.awt.*;

public class FreeButtonDetailPane extends ButtonWithHotkeysDetailPane<FreeButton> {
	private ButtonSytleDefinedPane stylePane;

	@Override
	protected Component createCenterPane() {
		return stylePane = new ButtonSytleDefinedPane();
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