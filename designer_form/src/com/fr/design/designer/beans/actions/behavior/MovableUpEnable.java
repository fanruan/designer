package com.fr.design.designer.beans.actions.behavior;

import com.fr.design.designer.beans.actions.FormWidgetEditAction;
import com.fr.design.mainframe.FormDesigner;

/**
 * Created by plough on 2018/1/19.
 */
public class MovableUpEnable implements UpdateBehavior {
    @Override
    public void doUpdate(FormWidgetEditAction action) {
        FormDesigner designer = action.getEditingComponent();
        if (designer == null) {
            action.setEnabled(false);
            return;
        }
        action.setEnabled(designer.isCurrentComponentMovableUp());
    }
}
