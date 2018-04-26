/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import java.awt.Component;
import java.awt.Image;

import javax.swing.SwingUtilities;

import com.fr.design.report.SelectImagePane;
import com.fr.design.dialog.DialogActionListener;
import com.fr.grid.Grid;
import com.fr.report.cell.FloatElement;

/**
 * FloatEditor used to edit Image object.
 */
public class ImageFloatEditor extends AbstractFloatEditor implements DialogActionListener{

    private SelectImagePane imageEditorPane = null;

    /**
     * Constructor.
     */
    public ImageFloatEditor() {
    }

    /**
     * Return the value of the FloatEditor.
     */
    @Override
    public Object getFloatEditorValue() throws Exception {
        return this.imageEditorPane.update();
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
        if (value == null || !(value instanceof Image)) {
            value = null;
        }

        this.imageEditorPane = new SelectImagePane();
        this.imageEditorPane.populate(floatElement);
        
        return this.imageEditorPane.showWindow(SwingUtilities.getWindowAncestor(grid), this);
    }

    @Override
	public void doOk() {
		stopFloatEditing();
		
	}

	@Override
	public void doCancel() {
		cancelFloatEditing();
		
	}
}