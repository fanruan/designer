package com.fr.design.mainframe.widget.accessibles;

import javax.swing.SwingUtilities;

import com.fr.general.FRFont;
import com.fr.design.mainframe.widget.wrappers.FontWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.style.FRFontPane;

public class AccessibleFontEditor extends UneditableAccessibleEditor {

    private FRFontPane frFontPane;

    public AccessibleFontEditor() {
        super(new FontWrapper());
    }
    
    @Override
    public FRFont getValue() {
    	if (super.getValue()==null) {
    		return FRFont.getInstance();
    	} else {
    		return (FRFont)super.getValue();
    	}
    }

    @Override
    protected void showEditorPane() {
        if (frFontPane == null) {
            frFontPane = new FRFontPane();
        }
        BasicDialog dlg = frFontPane.showWindow(SwingUtilities.getWindowAncestor(this));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(frFontPane.update());
                fireStateChanged();
            }
        });
        frFontPane.populate(this.getValue());
        dlg.setVisible(true);
    }
}