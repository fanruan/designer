/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit.order;

import com.fr.base.BaseUtils;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;

/**
 * Send FloatElement to back.
 */
public class SendFloatElementToBackAction extends AbstractFloatElementOrderAction {
    /**
     * Constructor
     */
	public SendFloatElementToBackAction(ElementCasePane t) {
		super(t);
		
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Edit_Send_To_Back"));
        this.setMnemonic('K');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/to_bottom.png"));
//		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, DEFAULT_MODIFIER + InputEvent.ALT_MASK));
	}

	@Override
	public void orderWithSelectedFloatElement(ElementCase report,
			FloatElement floatElement) {
		report.sendFloatElementToBack(floatElement);
	}
}
