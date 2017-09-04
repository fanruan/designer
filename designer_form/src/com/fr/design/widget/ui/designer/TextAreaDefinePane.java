package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.frpane.RegFieldPane;
import com.fr.design.gui.frpane.RegPane;
import com.fr.form.ui.TextArea;
import com.fr.form.ui.TextEditor;


public class TextAreaDefinePane extends TextFieldEditorDefinePane {
	public TextAreaDefinePane(XCreator xCreator) {
		super(xCreator);
	}

	protected RegFieldPane createRegPane() {
		return new RegFieldPane(RegPane.TEXTAREA_REG_TYPE);
	}
}