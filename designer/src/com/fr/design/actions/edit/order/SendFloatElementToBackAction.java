/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit.order;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * Send FloatElement to back.
 */
public class SendFloatElementToBackAction extends AbstractFloatElementOrderAction {
    /**
     * Constructor
     */
	public SendFloatElementToBackAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("M_Edit-Send_to_Back"));
        this.setMnemonic('K');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/to_bottom.png"));
		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, DEFAULT_MODIFIER + InputEvent.ALT_MASK));
	}

	@Override
	public void orderWithSelectedFloatElement(ElementCase report,
			FloatElement floatElement) {
		report.sendFloatElementToBack(floatElement);
	}
}