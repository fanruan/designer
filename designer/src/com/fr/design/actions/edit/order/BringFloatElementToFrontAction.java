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
 * Bring FloatElement to front.
 */
public class BringFloatElementToFrontAction extends AbstractFloatElementOrderAction {
    /**
     * Constructor.
     */
	public BringFloatElementToFrontAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("M_Edit-Bring_to_Front"));
        this.setMnemonic('t');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/bringToFront.png"));
    }

	@Override
	public void orderWithSelectedFloatElement(ElementCase report,
			FloatElement floatElement) {
		report.bringFloatElementToFront(floatElement);
	}
}