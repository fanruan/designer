package com.fr.design.designer.beans.actions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.FormDesigner;

public class CopyAction extends FormEditAction {

    public CopyAction(FormDesigner t) {
        super(t);
        this.setName(Inter.getLocText("M_Edit-Copy"));
        this.setMnemonic('C');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/copy.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
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