package com.fr.design.designer.beans.actions;

import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.designer.beans.actions.behavior.UpdateBehavior;
import com.fr.design.mainframe.FormDesigner;

import java.awt.event.ActionEvent;

public abstract class FormWidgetEditAction extends TemplateComponentAction<FormDesigner> {

    private UpdateBehavior updateBehavior = new UpdateBehavior<FormWidgetEditAction>() {
        @Override
        public void doUpdate(FormWidgetEditAction action) {
            action.setEnabled(true);
        }
    };

    protected FormWidgetEditAction(FormDesigner t) {
        super(t);
    }

    @Override
    public void update() {
        updateBehavior.doUpdate(this);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
    }

    public void setUpdateBehavior(UpdateBehavior updateBehavior) {
        this.updateBehavior = updateBehavior;
    }

}