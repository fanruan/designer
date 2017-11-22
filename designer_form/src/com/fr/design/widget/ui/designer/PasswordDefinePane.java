package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.frpane.RegFieldPane;
import com.fr.design.gui.frpane.RegPane;


public class PasswordDefinePane extends TextFieldEditorDefinePane {

	public PasswordDefinePane(XCreator xCreator) {
		super(xCreator);
	}
	private static final long serialVersionUID = 4737910705071750562L;

	protected RegFieldPane createRegPane() {
		return new RegFieldPane(RegPane.PASSWORD_REG_TYPE);
	}
}