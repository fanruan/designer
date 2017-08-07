package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.frpane.RegPane;
import com.fr.form.ui.Password;
import com.fr.form.ui.TextEditor;

public class PasswordDefinePane extends TextFieldEditorDefinePane {

	public PasswordDefinePane(XCreator xCreator) {
		super(xCreator);
	}
	private static final long serialVersionUID = 4737910705071750562L;

	@Override
	protected TextEditor newTextEditorInstance() {
		return new Password();
	}

	protected RegPane createRegPane() {
		return new RegPane(RegPane.PASSWORD_REG_TYPE);
	}
}