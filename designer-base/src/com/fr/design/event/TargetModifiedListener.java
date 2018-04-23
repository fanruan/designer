/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.event;

import java.util.EventListener;

/**
 * TemplateModifiedListener.
 */
public interface TargetModifiedListener extends EventListener {
    /**
     * Invoked when the target of the listener has changed the rpt content.
     */
    public void targetModified(TargetModifiedEvent e);
}