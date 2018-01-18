package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * 置于顶层（控件树内）
 * Created by plough on 2017/12/4.
 */

public class MoveToTopAction extends FormWidgetEditAction {

    public MoveToTopAction(FormDesigner t) {
        super(t);
        this.setName(Inter.getLocText("FR-Designer_Move_To_Top"));
        this.setMnemonic('T');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/to_top.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, DEFAULT_MODIFIER + InputEvent.SHIFT_MASK));
    }

    @Override
    protected String getToolTipText() {
        String originText = super.getToolTipText();
        return originText.replace(KeyEvent.getKeyText(KeyEvent.VK_CLOSE_BRACKET), "]");
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        FormDesigner designer = getEditingComponent();
        if (designer == null) {
            return false;
        }
        FormSelection selection = designer.getSelectionModel().getSelection();
        XCreator creator = selection.getSelectedCreator();
        XLayoutContainer container = (XLayoutContainer) creator.getParent();
        if (container.getComponentZOrder(creator) == 0) {
            return false;
        }
        container.setComponentZOrder(creator, 0);
        designer.getEditListenerTable().fireCreatorModified(creator, DesignerEvent.CREATOR_SELECTED);
        return true;
    }

    @Override
    public void update() {
        FormDesigner designer = getEditingComponent();
        if (designer == null) {
            this.setEnabled(false);
            return;
        }
        this.setEnabled(designer.isCurrentComponentMovableUp());
    }

}