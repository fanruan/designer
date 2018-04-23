/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file;

import java.awt.event.ActionEvent;

import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.KeySetUtils;

/**
 * Exit.
 */
public class ExitDesignerAction extends UpdateAction {
    /**
     * Constructor
     */
    public ExitDesignerAction() {
        this.setMenuKeySet(KeySetUtils.EXIT_DESIGNER);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
    }

    /**
     * 动作
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        DesignerContext.getDesignerFrame().exit();
    }
}