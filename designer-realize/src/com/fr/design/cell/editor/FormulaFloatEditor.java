/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import com.fr.base.BaseFormula;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.grid.Grid;
import com.fr.report.cell.FloatElement;

import javax.swing.*;
import java.awt.*;

/**
 * FloatEditor used to edit Formula object.
 */
public class FormulaFloatEditor extends AbstractFloatEditor {

    private UIFormula formulaEditorPane = null;

    /**
     * Constructor.
     */
    public FormulaFloatEditor() {
    }

    /**
     * Return the value of the FloatEditor.
     */
    @Override
    public Object getFloatEditorValue() throws Exception {
        BaseFormula newFormula = this.formulaEditorPane.update();
        if (newFormula.getContent() != null && newFormula.getContent().trim().length() > 1) {
            return newFormula;
        } else {
            return "";
        }
    }

    /**
     * Sets an initial <code>floatElement</code> for the editor.  This will cause
     * the editor to <code>stopFloatEditing</code> and lose any partially
     * edited value if the editor is editing when this method is called. <p>
     * <p/>
     * Returns the component that should be added to the client's
     * <code>Component</code> hierarchy.  Once installed in the client's
     * hierarchy this component will then be able to draw and receive
     * user input.
     *
     * @param grid        the <code>Grid</code> that is asking the
     *                    editor to edit; can be <code>null</code>
     * @param floatElement the value of the float to be edited; it is
     *                    up to the specific editor to interpret
     *                    and draw the value.
     */
    @Override
    public Component getFloatEditorComponent(Grid grid, FloatElement floatElement, int resolution) {
        //populate data to UI
        Object value = floatElement.getValue();
        if (value == null || !(value instanceof BaseFormula)) {
            value = BaseFormula.createFormulaBuilder().build();
        }

        this.formulaEditorPane = FormulaFactory.createFormulaPane();
        this.formulaEditorPane.populate((BaseFormula) value);

        return this.formulaEditorPane.showLargeWindow(SwingUtilities.getWindowAncestor(grid), new DialogActionAdapter() {

            @Override
            public void doOk() {
                FormulaFloatEditor.this.stopFloatEditing();
            }

            @Override
            public void doCancel() {
                FormulaFloatEditor.this.cancelFloatEditing();
            }
        });
    }
}