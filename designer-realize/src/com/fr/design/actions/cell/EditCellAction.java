/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ElementCaseAction;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;

/**
 * EditCellAction.
 */
public class EditCellAction extends ElementCaseAction {
	public EditCellAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("Edit"));
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