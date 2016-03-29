/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.accessibles;

import com.fr.base.Formula;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.mainframe.widget.wrappers.FormulaWrapper;

import javax.swing.*;

/**
 * @author richer
 * @since 6.5.3
 */
public class AccessibleFormulaEditor extends BaseAccessibleEditor {

    private UIFormula formulaPane;

    public AccessibleFormulaEditor() {
        super(new FormulaWrapper(), new FormulaWrapper(), true);
    }

    @Override
    protected void showEditorPane() {
        if (formulaPane == null) {
            formulaPane = FormulaFactory.createFormulaPane();
        }
        BasicDialog dlg = formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(this),new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(formulaPane.update());
                fireStateChanged();
            }
        });
        formulaPane.populate((Formula) getValue());
        dlg.setVisible(true);
    }
}