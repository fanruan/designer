package com.fr.design.widget.ui.btn;

import java.awt.Component;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.component.ButtonBackgroundPane;
import com.fr.form.ui.FreeButton;
import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;
import com.fr.general.Inter;

import javax.swing.*;

public class FreeButtonDetailPane extends ButtonWithHotkeysDetailPane<FreeButton> {
	private ButtonBackgroundPane backgroundCompPane;

	@Override
	protected Component createCenterPane() {
		backgroundCompPane = new ButtonBackgroundPane();
		JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{new UILabel(Inter.getLocText("FR-Designer_Background") + ":"), backgroundCompPane}}, TableLayoutHelper.FILL_LASTCOLUMN, 18, 7);
		jPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		return jPanel;
	}
	
	@Override
	public FreeButton createButton() {
		return new FreeButton();
	}

	public void populate(FreeButton button) {
		super.populate(button);
		backgroundCompPane.populate(button);
	}

	@Override
	public FreeButton update() {
		FreeButton button = super.update();
		backgroundCompPane.update(button);
		return button;
	}

	@Override
	public Class classType() {
		return FreeButton.class;
	}
}