package com.fr.design.fun.impl;

import com.fr.design.actions.UpdateAction;
import com.fr.design.designer.TargetComponent;
import com.fr.design.fun.RightSelectionHandlerProvider;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.BaseFormDesigner;
import com.fr.design.selection.SelectableElement;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

import java.util.List;

@API(level = RightSelectionHandlerProvider.CURRENT_LEVEL)
public abstract class AbstractRightSelectionHandlerProvider extends AbstractProvider implements RightSelectionHandlerProvider {
    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    @Override
    public void dmlUpdateActions(BaseFormDesigner formDesigner, List<UpdateAction> actions) {

    }

    @Override
    public boolean accept(BaseFormDesigner formDesigner) {
        return false;
    }

    @Override
    public void dmlMenu(TargetComponent ePane, UIPopupMenu popupMenu) {

    }

    @Override
    public boolean accept(SelectableElement selectableElement) {
        return false;
    }
}