/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit.clear;


import com.fr.design.mainframe.ElementCasePane;

/**
 * Clear formats.
 */
public class ClearFormatsAction extends ClearAction {
    /**
     * Constructor
     */
    public ClearFormatsAction(ElementCasePane t) {
		super(t);
		
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Edit_Clear_Formats"));
        this.setMnemonic('F');
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        ElementCasePane reportPane = getEditingComponent();
        if (reportPane == null) {
            return false;
        }
        return reportPane.clearFormats();
    }
}
