/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ElementCaseAction;

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
		
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Edit_Delete"));
        this.setMnemonic('D');
        //Richie:删除菜单图标
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/delete.png"));
//        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
    }
    
    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        ElementCasePane ePane =  getEditingComponent();
        
        return ePane.getSelection().triggerDeleteAction(ePane);
    }
}
