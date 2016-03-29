/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit.order;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;

/**
 * Bring FloatElement forward.
 */
public class BringFloatElementForwardAction extends AbstractFloatElementOrderAction {
    /**
     * Constructor.
     */
	public BringFloatElementForwardAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("M_Edit-Bring_Forward"));
        this.setMnemonic('F');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/bringForward.png"));
    }

	@Override
	public void orderWithSelectedFloatElement(ElementCase report,
			FloatElement floatElement) {
		report.bringFloatElementForward(floatElement);
	}
}