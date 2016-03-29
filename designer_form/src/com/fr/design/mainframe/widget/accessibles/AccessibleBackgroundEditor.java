/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.accessibles;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import com.fr.general.Background;
import com.fr.design.mainframe.widget.wrappers.BackgroundWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.style.background.BackgroundPane;

/**
 * @author richer
 * @since 6.5.3
 */
public class AccessibleBackgroundEditor extends UneditableAccessibleEditor {

    private BackgroundPane backgroundPane;

    public AccessibleBackgroundEditor() {
        super(new BackgroundWrapper());
    }

    @Override
    protected void showEditorPane() {
        if (backgroundPane == null) {
            backgroundPane = new BackgroundPane();
            backgroundPane.setPreferredSize(new Dimension(600, 400));
        }
        BasicDialog dlg = backgroundPane.showWindow(SwingUtilities.getWindowAncestor(this));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(backgroundPane.update());
                fireStateChanged();
            }
        });
        backgroundPane.populate((Background) getValue());
        dlg.setVisible(true);
    }
}