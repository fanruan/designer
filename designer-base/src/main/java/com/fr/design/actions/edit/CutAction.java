/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.designer.TargetComponent;


import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * Cut.
 */
public class CutAction extends TemplateComponentAction {
    /**
     * Constructor
     */
	public CutAction(TargetComponent t) {
    	super(t);
    	
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_M_Edit_Cut"));
        this.setMnemonic('T');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/cut.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, DEFAULT_MODIFIER));
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        TargetComponent editPane = getEditingComponent();
        if (editPane == null) {
            return false;
        }
        return editPane.cut();
    }
}