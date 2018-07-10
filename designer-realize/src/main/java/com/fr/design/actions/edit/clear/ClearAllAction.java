/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit.clear;

import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;

/**
 * Clear all.
 */
public class ClearAllAction extends ClearAction {
    /**
     * Constructor
     */
    public ClearAllAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("M_Edit-Clear_All"));
        this.setMnemonic('A');
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        ElementCasePane reportPane = getEditingComponent();
        if (reportPane == null) {
            return false;
        }
        return reportPane.clearAll();
    }
}