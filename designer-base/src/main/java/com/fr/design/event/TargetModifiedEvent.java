/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.event;

import java.util.EventObject;

/**
 * ReportDataChangeEvent.
 */
public class TargetModifiedEvent extends EventObject {

    /**
     * Constructs a CellSelectionChangeEvent object.
     */
    public TargetModifiedEvent(Object source) {
        super(source);
    }
}