/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit.clear;


import com.fr.design.mainframe.ElementCasePane;

/**
 * Clear contents.
 */
public class ClearContentsAction extends ClearAction {
    /**
     * Constructor
     */
	public ClearContentsAction(ElementCasePane t) {
		super(t);
		
        this.setName(com.fr.design.i18n.Toolkit.i18nText("M_Edit-Clear_Contents"));
        this.setMnemonic('C');
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        ElementCasePane reportPane = getEditingComponent();
        if (reportPane == null) {
            return false;
        }
        return reportPane.clearContents();
    }
}