package com.fr.design.mainframe.widget.accessibles;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import com.fr.design.mainframe.widget.editors.DimensionEditingPane;
import com.fr.design.mainframe.widget.wrappers.DimensionWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;

/**
 * Size编辑器
 * @since 6.5.3
 */
public class AccessibleDimensionEditor extends BaseAccessibleEditor {

    private DimensionEditingPane dimensionPane;

    public AccessibleDimensionEditor() {
        super(new DimensionWrapper(), new DimensionWrapper(), true);
    }

    @Override
    protected void showEditorPane() {
        if (dimensionPane == null) {
            dimensionPane = new DimensionEditingPane();
        }
        BasicDialog dlg = dimensionPane.showWindow(SwingUtilities.getWindowAncestor(this));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(dimensionPane.update());
                fireStateChanged();
            }
        });
        dimensionPane.populate((Dimension) getValue());
        dlg.setVisible(true);
    }
}