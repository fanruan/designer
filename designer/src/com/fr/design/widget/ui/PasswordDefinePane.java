package com.fr.design.widget.ui;

import com.fr.design.gui.frpane.RegFieldPane;
import com.fr.design.gui.frpane.RegPane;
import com.fr.form.ui.Password;
import com.fr.form.ui.TextEditor;

public class PasswordDefinePane extends TextFieldEditorDefinePane {
	private static final long serialVersionUID = 4737910705071750562L;

	@Override
	protected TextEditor newTextEditorInstance() {
		return new Password();
	}

	protected RegFieldPane createRegPane() {
		return new RegFieldPane(RegPane.PASSWORD_REG_TYPE);
	}
}