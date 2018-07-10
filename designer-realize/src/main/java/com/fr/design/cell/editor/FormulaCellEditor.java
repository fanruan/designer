/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import com.fr.base.BaseFormula;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

import javax.swing.*;
import java.awt.*;

/**
 * CellEditor used to edit Formula object.
 */
public class FormulaCellEditor extends com.fr.design.cell.editor.AbstractCellEditor {

    private UIFormula formulaEditorPane = null;

    /**  
     * Constructor.
     */
    public FormulaCellEditor(ElementCasePane<? extends TemplateElementCase> ePane) {
    	super(ePane);
    }

    /**
     * Return the value of the CellEditor.
     */
    @Override
    public Object getCellEditorValue() throws Exception {
        BaseFormula newFormula = this.formulaEditorPane.update();
        if (newFormula.getContent() != null && newFormula.getContent().trim().length() > 1) {
            return newFormula;
        } else {
            return "";
        }
    }

    /**
     * Sets an initial <code>cellElement</code> for the editor.  This will cause
     * the editor to <code>stopCellEditing</code> and lose any partially
     * edited value if the editor is editing when this method is called. <p>
     * <p/>
     * Returns the component that should be added to the client's
     * <code>Component</code> hierarchy.  Once installed in the client's
     * hierarchy this component will then be able to draw and receive
     * user input.
     *
     * @param grid        the <code>Grid</code> that is asking the
     *                    editor to edit; can be <code>null</code>
     * @param cellElement the value of the cell to be edited; it is
     *                    up to the specific editor to interpret
     *                    and draw the value.
     */
    @Override
    public Component getCellEditorComponent(Grid grid, TemplateCellElement cellElement, int resolution) {
        Object value = null;
        if (cellElement != null) {
            value = cellElement.getValue();
        }
        if (value == null || !(value instanceof BaseFormula)) {
            value = BaseFormula.createFormulaBuilder().build();
        }

        this.formulaEditorPane = FormulaFactory.createFormulaPaneWhenReserveFormula();

        this.formulaEditorPane.populate((BaseFormula) value);
        return this.formulaEditorPane.showLargeWindow(SwingUtilities.getWindowAncestor(grid), new DialogActionAdapter() {

            @Override
            public void doOk() {
                FormulaCellEditor.this.stopCellEditing();
            }

            @Override
            public void doCancel() {
                FormulaCellEditor.this.cancelCellEditing();
            }
        });
    }
}