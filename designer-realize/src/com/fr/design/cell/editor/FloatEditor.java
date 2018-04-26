/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import java.awt.Component;
import java.awt.event.KeyEvent;

import com.fr.grid.Grid;
import com.fr.grid.event.FloatEditorListener;
import com.fr.report.cell.FloatElement;

/**
 * FloatEditor interface to edit FloatElement.
 */
public interface FloatEditor {
    /**
     * Returns the value contained in the editor.
     *
     * @return the value contained in the editor
     */
    public Object getFloatEditorValue() throws Exception;

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
     * @param floatElement the value of the FloatElement to be edited; it is
     *                    up to the specific editor to interpret
     *                    and draw the value.
     */
    public Component getFloatEditorComponent(Grid grid, FloatElement floatElement, int resolution);

    /**
     * Accept keyevent to start editing.
     */
    public boolean acceptKeyEventToStartFloatEditing(KeyEvent evt);

    /**
     * Tells the editor to stop editing and accept any partially edited
     * value as the value of the editor.  The editor returns false if
     * editing was not stopped; this is useful for editors that validate
     * and can not accept invalid entries.
     *
     * @return true if editing was stopped; false otherwise
     */
    public boolean stopFloatEditing();

    /**
     * Tells the editor to cancel editing and not accept any partially
     * edited value.
     */
    public void cancelFloatEditing();

    /**
     * Adds a listener to the list that's notified when the editor
     * stops, or cancels editing.
     *
     * @param floatEditorListener the FloatEditorListener
     */
    public void addFloatEditorListener(FloatEditorListener floatEditorListener);

    /**
     * Removes a listener from the list that's notified
     *
     * @param floatEditorListener the FloatEditorListener
     */
    public void removeFloatEditorListener(FloatEditorListener floatEditorListener);
}