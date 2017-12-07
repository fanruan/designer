package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * 下移一层（控件树内）
 * Created by plough on 2017/12/4.
 */

public class MoveDownAction extends FormEditAction {

    public MoveDownAction(FormDesigner t) {
        super(t);
        this.setName(Inter.getLocText("FR-Designer_Move_Down"));
        this.setMnemonic('T');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/down.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, DEFAULT_MODIFIER));
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        FormDesigner designer = getEditingComponent();
        if (designer == null) {
            return false;
        }
        FormSelection selection = designer.getSelectionModel().getSelection();
        XCreator creator = selection.getSelectedCreator();
        Container container = creator.getParent();
        int targetIndex = container.getComponentZOrder(creator) + 1;
        if (targetIndex >= container.getComponentCount()) {
            return false;
        }
        container.setComponentZOrder(creator, targetIndex);
        designer.getEditListenerTable().fireCreatorModified(creator, DesignerEvent.CREATOR_DELETED);
        return true;
    }

    @Override
    public void update() {
        FormDesigner designer = getEditingComponent();
        if (designer == null) {
            this.setEnabled(false);
            return;
        }
        this.setEnabled(designer.isCurrentComponentMovableDown());
    }
}