/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.present.ConditionAttributesGroupPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.TemplateCellElement;

/**
 * Condition Attributes.
 */
public class ConditionAttributesAction extends AbstractCellElementAction {
	public ConditionAttributesAction(ElementCasePane t) {
		super(t);
        this.setMenuKeySet(KeySetUtils.CONDITION_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/highlight.png"));
    }

	@Override
	protected BasicPane populateBasicPane(TemplateCellElement cellElement) {
        ConditionAttributesGroupPane pane = new ConditionAttributesGroupPane();
        pane.populate(cellElement.getHighlightGroup());
        
		return pane;
	}

	@Override
	protected void updateBasicPane(BasicPane bp, TemplateCellElement cellElement) {
        cellElement.setHighlightGroup(((ConditionAttributesGroupPane)bp).updateHighlightGroup());		
	}
}