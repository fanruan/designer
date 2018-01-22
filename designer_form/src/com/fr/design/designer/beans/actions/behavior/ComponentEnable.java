package com.fr.design.designer.beans.actions.behavior;

import com.fr.design.designer.beans.actions.FormWidgetEditAction;
import com.fr.design.mainframe.FormDesigner;

/**
 * 只对控件有效，对底层布局（form/body）无效
 * Created by plough on 2018/1/19.
 */
public class ComponentEnable implements UpdateBehavior<FormWidgetEditAction> {
    @Override
    public void doUpdate(FormWidgetEditAction action) {
        FormDesigner designer = action.getEditingComponent();
        if (designer == null) {
            action.setEnabled(false);
            return;
        }
        action.setEnabled(!designer.isRootSelected());
    }
}
