package com.fr.design.widget.ui;

import com.fr.design.gui.frpane.RegPane;
import com.fr.form.ui.TextArea;
import com.fr.form.ui.TextEditor;


public class TextAreaDefinePane extends TextFieldEditorDefinePane {

	@Override
	protected TextEditor newTextEditorInstance() {
		return new TextArea();
	}

	protected RegPane createRegPane() {
		return new RegPane(RegPane.TEXTAREA_REG_TYPE);
	}
}