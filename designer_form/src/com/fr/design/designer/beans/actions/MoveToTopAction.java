package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.FormDesigner;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * 置于顶层（控件树内）
 * Created by plough on 2017/12/4.
 */

public class MoveToTopAction extends FormEditAction {

    public MoveToTopAction(FormDesigner t) {
        super(t);
        this.setName(Inter.getLocText("FR-Designer_Move_To_Top"));
        this.setMnemonic('T');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/up.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, DEFAULT_MODIFIER + InputEvent.ALT_MASK));
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