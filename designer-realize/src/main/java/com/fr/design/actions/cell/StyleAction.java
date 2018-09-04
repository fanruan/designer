/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell;


import java.awt.event.ActionEvent;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.menu.KeySetUtils;


public class StyleAction extends UpdateAction {

	public StyleAction() {
    	
        this.setMenuKeySet(KeySetUtils.GLOBAL_STYLE);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
	}

    /**
     * 动作
     * @param e 事件
     */
	public void actionPerformed(ActionEvent e) {
		CellElementPropertyPane.getInstance().GoToPane(new String[] { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Custom") });
	}

}