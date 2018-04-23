/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file;

import java.awt.event.ActionEvent;

import com.fr.design.actions.JTemplateAction;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.Inter;

/**
 * Close Report.
 */
public class CloseTemplateAction extends JTemplateAction<JTemplate<?, ?>> {
    /**
     * Constructor
     */
    public CloseTemplateAction(JTemplate<?, ?> jt) {
    	super(jt);
    	
        this.setName(Inter.getLocText("M-Close_Template"));
        this.setMnemonic('C');
    }

    public void actionPerformed(ActionEvent evt) {
//    	this.getEditingComponent().unlockTemplate();
    }
}