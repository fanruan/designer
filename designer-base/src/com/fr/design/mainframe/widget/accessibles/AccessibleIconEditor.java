package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.renderer.IconCellRenderer;
import com.fr.design.mainframe.widget.wrappers.IconWrapper;
import com.fr.design.web.CustomIconPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;


public class AccessibleIconEditor extends UneditableAccessibleEditor {

    private CustomIconPane customIconPane;

    public AccessibleIconEditor() {
        super(new IconWrapper());
    }
    
    @Override
	protected ITextComponent createTextField() {
        return new RendererField(new IconCellRenderer());
    }

    @Override
    protected void showEditorPane() {
        if (customIconPane == null) {
            customIconPane = new CustomIconPane();
        }
        BasicDialog editDialog = customIconPane.showWindow(DesignerContext.getDesignerFrame());
        editDialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(customIconPane.update());
                fireStateChanged();
            }
        });
        customIconPane.populate((String) this.getValue());
        editDialog.setVisible(true);
    }
}