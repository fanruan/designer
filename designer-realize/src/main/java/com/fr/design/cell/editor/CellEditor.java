/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import java.awt.Component;
import java.awt.Point;

import com.fr.grid.Grid;
import com.fr.grid.event.CellEditorListener;
import com.fr.report.cell.TemplateCellElement;

/**
 * CellEditor interface.
 */
public interface CellEditor {
    /**
     * Returns the value contained in the editor.
     *
     * @return the value contained in the editor
     */
    public Object getCellEditorValue() throws Exception;

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
     * @param resolution  the current screen resolution
     */
    public Component getCellEditorComponent(Grid grid, TemplateCellElement cellElement, int resolution);

    /**
     * Gets the location of this component in the form of a point
     * specifying the component's top-left corner in the CellElement's
     * coordinate space. The default value is (0, 0).
     * @return an instance of <code>Point</code> representing
     * 		the top-left corner of the component's bounds in the
     * 		coordinate space of the CellElement    
     */
    public Point getLocationOnCellElement();

    /**
     * Sets the location of this component in the form of a point
     * specifying the component's top-left corner in the CellElement's
     * coordinate space.
     * @param loc an instance of <code>Point</code> representing
     * 		the top-left corner of the component's bounds in the
     * 		coordinate space of the CellElement
     */
    public void setLocationOnCellElement(Point loc);

    /**
     * Tells the editor to stop editing and accept any partially edited
     * value as the value of the editor.  The editor returns false if
     * editing was not stopped; this is useful for editors that validate
     * and can not accept invalid entries.
     *
     * @return true if editing was stopped; false otherwise
     */
    public boolean stopCellEditing();

    /**
     * Tells the editor to cancel editing and not accept any partially
     * edited value.
     */
    public void cancelCellEditing();

    /**
     * Adds a listener to the list that's notified when the editor
     * stops, or cancels editing.
     *
     * @param cellEditorListener the CellEditorListener
     */
    public void addCellEditorListener(CellEditorListener cellEditorListener);

    /**
     * Removes a listener from the list that's notified
     *
     * @param cellEditorListener the CellEditorListener
     */
    public void removeCellEditorListener(CellEditorListener cellEditorListener);
}