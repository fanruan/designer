package com.fr.design.widget.ui;

import com.fr.design.gui.frpane.RegFieldPane;
import com.fr.design.gui.frpane.RegPane;
import com.fr.form.ui.TextArea;
import com.fr.form.ui.TextEditor;


public class TextAreaDefinePane extends TextFieldEditorDefinePane {

	@Override
	protected TextEditor newTextEditorInstance() {
		return new TextArea();
	}

	protected RegFieldPane createRegPane() {
		return new RegFieldPane(RegPane.TEXTAREA_REG_TYPE);
	}
}