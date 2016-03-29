/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ElementCaseAction;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;

/**
 * Delete Row, Column or FloatElement..
 */
public class DeleteAction extends ElementCaseAction {
    /**
     * Constructor
     */
    public DeleteAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("M_Edit-Delete") + "...");
        this.setMnemonic('D');
        //Richie:删除菜单图标
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/delete.png"));
    }
    
    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        ElementCasePane ePane =  getEditingComponent();
        
        return ePane.getSelection().triggerDeleteAction(ePane);
    }
}