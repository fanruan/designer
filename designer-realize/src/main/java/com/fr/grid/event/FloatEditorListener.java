/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.grid.event;

import java.util.EventListener;

/**
 * FloatEditorListener used for FloatEditor start, stop and cancel..
 */
public interface FloatEditorListener extends EventListener {
    /**
     * This tells the listeners the editor has stopped editing
     */
    public void editingStopped(FloatEditorEvent evt);

    /**
     * This tells the listeners the editor has canceled editing
     */
    public void editingCanceled(FloatEditorEvent evt);
}