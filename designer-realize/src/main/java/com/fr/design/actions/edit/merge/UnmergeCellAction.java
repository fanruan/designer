/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit.merge;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.mainframe.ElementCasePane;

/**
 * unmerge cell..
 */
public class UnmergeCellAction extends ElementCaseAction {
    /**
     * Constructor
     */
	public UnmergeCellAction(ElementCasePane t) {
    	super(t);
        this.setMenuKeySet(KeySetUtils.UNMERGE_CELL);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/unmerge.png"));
    }

	@Override
	public void update() {
		super.update();
		ElementCasePane reportPane = getEditingComponent();
		this.setEnabled(reportPane.canUnMergeCell());
	}

    /**
     * 执行动作
     * @return 是则返回true
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        ElementCasePane reportPane = getEditingComponent();
        if (reportPane == null) {
            return false;
        }
        return reportPane.unMergeCell();
    }

}