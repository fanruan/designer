/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.grid.event;

import java.util.EventListener;

/**
 * CellEditorListener used for CellEditor start, stop and cancel..
 */
public interface CellEditorListener extends EventListener {
    /**
     * This tells the listeners the editor has stopped editing
     */
    public void editingStopped(CellEditorEvent evt);

    /**
     * This tells the listeners the editor has canceled editing
     */
    public void editingCanceled(CellEditorEvent evt);
}