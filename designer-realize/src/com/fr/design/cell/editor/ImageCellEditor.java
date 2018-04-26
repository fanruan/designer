/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import java.awt.Component;
import java.awt.Image;

import javax.swing.SwingUtilities;

import com.fr.design.report.SelectImagePane;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * CellEditor used to edit Image object.
 */
public class ImageCellEditor extends AbstractCellEditor implements DialogActionListener {

    private SelectImagePane imageEditorPane = null;

    /**
     * Constructor.
     */
    public ImageCellEditor(ElementCasePane<? extends TemplateElementCase> ePane) {
    	super(ePane);
    }

    /**
     * Return the value of the CellEditor.
     */
    @Override
    public Object getCellEditorValue() throws Exception {
        return this.imageEditorPane.update();
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
        if (value == null || !(value instanceof Image)) {
            value = null;
        }

        this.imageEditorPane = new SelectImagePane();
        this.imageEditorPane.populate(cellElement);
        
        return this.imageEditorPane.showWindow(SwingUtilities.getWindowAncestor(grid), this);
    }

	@Override
	public void doOk() {
		stopCellEditing();
		
	}

	@Override
	public void doCancel() {
		cancelCellEditing();
		
	}
}