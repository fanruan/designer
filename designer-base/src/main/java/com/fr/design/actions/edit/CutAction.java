/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit;

import com.fr.base.BaseUtils;
import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.designer.TargetComponent;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

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
        this.setEnabled(!DesignModeContext.isBanCopyAndCut());
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        return DesignModeContext.doPaste(getEditingComponent());
    }
}