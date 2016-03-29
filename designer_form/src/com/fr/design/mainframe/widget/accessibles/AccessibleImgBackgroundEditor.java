package com.fr.design.mainframe.widget.accessibles;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import com.fr.base.background.ImageBackground;
import com.fr.design.mainframe.widget.wrappers.BackgroundWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.ImgChoosePane;

public class AccessibleImgBackgroundEditor extends UneditableAccessibleEditor {
	private ImgChoosePane choosePane;
	
	public AccessibleImgBackgroundEditor() {
		super(new BackgroundWrapper());
	}
	
	@Override
	protected void showEditorPane() {
		if (choosePane == null) {
			choosePane = new ImgChoosePane();
			choosePane.setPreferredSize(new Dimension(600, 400));
		}
		BasicDialog dlg = choosePane.showWindow(SwingUtilities.getWindowAncestor(this));
		dlg.addDialogActionListener(new DialogActionAdapter() {

			@Override
			public void doOk() {
				setValue(choosePane.update());
				fireStateChanged();
			}
		});
		choosePane.populate(getValue() instanceof ImageBackground ? (ImageBackground) getValue() : null);
		dlg.setVisible(true);
	}
}