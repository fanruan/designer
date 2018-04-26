package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.FormDesigner;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

public class CopyAction extends FormEditAction {

    public CopyAction(FormDesigner t) {
        super(t);
        this.setName(Inter.getLocText("M_Edit-Copy"));
        this.setMnemonic('C');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/copy.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, DEFAULT_MODIFIER));
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