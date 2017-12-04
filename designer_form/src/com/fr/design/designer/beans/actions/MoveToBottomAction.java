package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.FormDesigner;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * 置于底层（控件树内）
 * Created by plough on 2017/12/4.
 */

public class MoveToBottomAction extends FormEditAction {

    public MoveToBottomAction(FormDesigner t) {
        super(t);
        this.setName(Inter.getLocText("FR-Designer_Move_To_Bottom"));
        this.setMnemonic('T');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/down.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, DEFAULT_MODIFIER + InputEvent.ALT_MASK));
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        FormDesigner editPane = getEditingComponent();
        if (editPane == null) {
            return false;
        }
        return editPane.cut();
    }

}