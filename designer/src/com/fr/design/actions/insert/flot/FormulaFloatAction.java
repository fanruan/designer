/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.flot;

import com.fr.base.BaseUtils;
import com.fr.base.Formula;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.report.cell.FloatElement;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Insert formula.
 */
public class FormulaFloatAction extends AbstractShapeAction {
	public FormulaFloatAction(ElementCasePane t) {
		super(t);
        this.setMenuKeySet(FLOAT_INSERT_FORMULA);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/formula.png"));
    }

    public static final MenuKeySet FLOAT_INSERT_FORMULA = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'F';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("HF-Insert_Formula");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    /**
     * 动作
     * @param e 事件
     */
	public void actionPerformed(ActionEvent e) {
    	ElementCasePane jws = getEditingComponent();
        if (jws == null) {
            return;
        }
        
        // 
        FloatElement floatElement = new FloatElement( new Formula(""));
        this.startDraw(floatElement);
    }
}