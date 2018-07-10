package com.fr.design.mainframe.widget.accessibles;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.swing.SwingUtilities;

import com.fr.data.core.FormatField.FormatContents;
import com.fr.design.mainframe.widget.wrappers.DateWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.style.FormatPane;

public class AccessibleDateEditor extends UneditableAccessibleEditor {

	private FormatPane formatPane;
	
	public AccessibleDateEditor() {
		super(new DateWrapper());
	}

	@Override
	protected void showEditorPane() {
		if (formatPane == null) {
			formatPane = new FormatPane();
		}
		formatPane.setStyle4Pane(new int[]{FormatContents.DATE,FormatContents.TIME});
		BasicDialog dlg = formatPane.showWindow(
				SwingUtilities.getWindowAncestor(this));
		formatPane.populate(new SimpleDateFormat((String) getValue()));
		dlg.addDialogActionListener(new DialogActionAdapter() {
			public void doOk() {
				Format format = formatPane.update();
				setValue(((SimpleDateFormat) format).toPattern());
				fireStateChanged();
			}
		});
		dlg.setVisible(true);
	}
}