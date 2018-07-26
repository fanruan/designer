package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.actions.behavior.ComponentEnable;
import com.fr.design.mainframe.FormDesigner;


import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

public class CopyAction extends FormWidgetEditAction {

    public CopyAction(FormDesigner t) {
        super(t);
        this.setName(com.fr.design.i18n.Toolkit.i18nText("M_Edit-Copy"));
        this.setMnemonic('C');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/copy.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, DEFAULT_MODIFIER));
        setUpdateBehavior(new ComponentEnable());
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        FormDesigner tc = getEditingComponent();
        if (tc != null) {
            tc.copy();
        }
        return false;
    }
}