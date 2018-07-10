/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import java.awt.event.KeyEvent;

import javax.swing.event.EventListenerList;

import com.fr.grid.event.FloatEditorEvent;
import com.fr.grid.event.FloatEditorListener;

/**
 * The baics abstract float editor.
 */
public abstract class AbstractFloatEditor implements FloatEditor {
    private EventListenerList listenerList = new EventListenerList();
    private FloatEditorEvent floatEditEvent = null;

    /**
     * Accept keyevent to start editing.
     */
    public boolean acceptKeyEventToStartFloatEditing(KeyEvent evt) {
        return true;
    }

    /**
     * Tells the editor to stop editing and accept any partially edited
     * value as the value of the editor.  The editor returns false if
     * editing was not stopped; this is useful for editors that validate
     * and can not accept invalid entries.
     *
     * @return true if editing was stopped; false otherwise
     */
    public boolean stopFloatEditing() {
        fireEditingStopped();
        return true;
    }

    /**
     * Tells the editor to cancel editing and not accept any partially
     * edited value.
     */
    public void cancelFloatEditing() {
        fireEditingCanceled();
    }

    /**
     * Adds a listener to the list that's notified when the editor
     * stops, or cancels editing.
     *
     * @param floatEditorListener the FloatEditorListener
     */
    public void addFloatEditorListener(FloatEditorListener floatEditorListener) {
        listenerList.add(FloatEditorListener.class, floatEditorListener);
    }

    /**
     * Removes a listener from the list that's notified
     *
     * @param floatEditorListener the FloatEditorListener
     */
    public void removeFloatEditorListener(FloatEditorListener floatEditorListener) {
        listenerList.remove(FloatEditorListener.class, floatEditorListener);
    }

    /**
     * Fire editing stopped listeners.
     */
    protected void fireEditingStopped() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FloatEditorListener.class) {
                // Lazily create the event:
                if (floatEditEvent == null) {
                    floatEditEvent = new FloatEditorEvent(this);
                }

                ((FloatEditorListener) listeners[i + 1]).editingStopped(floatEditEvent);
            }
        }
    }

    /**
     * Fire editing canceled listeners.
     */
    protected void fireEditingCanceled() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FloatEditorListener.class) {
                // Lazily create the event:
                if (floatEditEvent == null) {
                    floatEditEvent = new FloatEditorEvent(this);
                }

                ((FloatEditorListener) listeners[i + 1]).editingCanceled(floatEditEvent);
            }
        }
    }
}