/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.mainframe.ElementCasePane;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

/**
 * EditCellAction.
 */
public class EditCellAction extends ElementCaseAction {
	public EditCellAction(ElementCasePane t) {
		super(t);
		
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit"));
        this.setMnemonic('I');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/edit.png"));
    }

    @Override
	public void actionPerformed(ActionEvent evt) {
        ElementCasePane reportPane = getEditingComponent();
    	if(reportPane.isSelectedOneCell()){
            reportPane.getGrid().startEditing();
    	}else{
    		Toolkit.getDefaultToolkit().beep();
    	}
    }
    
    // TODO ALEX_SEP 这里的undo redo又是怎么回事呢?
    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
    	return false;
    }
}
