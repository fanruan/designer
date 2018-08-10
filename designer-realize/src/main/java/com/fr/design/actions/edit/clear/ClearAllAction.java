/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit.clear;


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
		
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Edit_Clear_All"));
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
