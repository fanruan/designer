/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.accessibles;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.renderer.LayoutBorderStyleRenderer;
import com.fr.design.mainframe.widget.wrappers.LayoutBorderStyleWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.xpane.CardTagLayoutBorderPane;
import com.fr.form.ui.LayoutBorderStyle;

public class AccessibleCardTagWLayoutBorderStyleEditor extends UneditableAccessibleEditor {
    private CardTagLayoutBorderPane borderPane;
    
    public AccessibleCardTagWLayoutBorderStyleEditor() {
        super(new LayoutBorderStyleWrapper());
    }
    
    @Override
    protected ITextComponent createTextField() {
        return new RendererField(new LayoutBorderStyleRenderer());
    }
    
    @Override
    protected void showEditorPane() {
        if (borderPane == null) {
            borderPane = new CardTagLayoutBorderPane();
            borderPane.setPreferredSize(new Dimension(600, 400));
        }
        BasicDialog dlg = borderPane.showWindow(SwingUtilities.getWindowAncestor(this));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(borderPane.update());
                fireStateChanged();
            }
        });
        borderPane.populate((LayoutBorderStyle)getValue());
        dlg.setVisible(true);
    }
}