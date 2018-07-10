/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.editor.editor;

import java.util.EventListener;

import javax.swing.event.ChangeEvent;

/**
 * CellEditorListener used for CellEditor start, stop and cancel..
 */
public interface EditorListener extends EventListener {

    /**
     * This tells the listeners the editor has started editing.
     */
    public void editingStarted(ChangeEvent evt);

    /**
     * This tells the listeners the editor has stopped editing
     */
    public void editingStopped(ChangeEvent evt);

    /**
     * This tells the listeners the editor has canceled editing
     */
    public void editingCanceled(ChangeEvent evt);
}