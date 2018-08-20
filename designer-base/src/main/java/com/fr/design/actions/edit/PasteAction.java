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
 * Paste.
 */
public class PasteAction extends TemplateComponentAction {
    /**
     * Constructor
     */
	public PasteAction(TargetComponent t) {
    	super(t);
    	
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_M_Edit_Paste"));
        this.setMnemonic('P');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/paste.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, DEFAULT_MODIFIER));
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        TargetComponent tc = getEditingComponent();
        if (tc == null) {
            return false;
        }
        return tc.paste();
    }
}