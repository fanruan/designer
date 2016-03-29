/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.accessibles;

import javax.swing.SwingUtilities;

import com.fr.design.mainframe.widget.editors.PaddingMarginPane;
import com.fr.design.mainframe.widget.wrappers.PaddingMarginWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.form.ui.PaddingMargin;

/**
 * @author richer
 * @since 6.5.3
 */
public class AccessilePaddingMarginEditor extends BaseAccessibleEditor {

    private PaddingMarginPane pane;

    public AccessilePaddingMarginEditor() {
        super(new PaddingMarginWrapper(), new PaddingMarginWrapper(), true);
    }

    @Override
    protected void showEditorPane() {
        if (pane == null) {
            pane = new PaddingMarginPane();
        }
		BasicDialog dlg = pane.showSmallWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

			@Override
			public void doOk() {
				setValue(pane.update());
				fireStateChanged();
			}
		});
        pane.populate((PaddingMargin) getValue());
        dlg.setVisible(true);
    }
}