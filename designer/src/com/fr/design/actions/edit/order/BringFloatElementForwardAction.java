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
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

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
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/up.png"));
//		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, DEFAULT_MODIFIER));
	}

	@Override
	public void orderWithSelectedFloatElement(ElementCase report,
			FloatElement floatElement) {
		report.bringFloatElementForward(floatElement);
	}
}