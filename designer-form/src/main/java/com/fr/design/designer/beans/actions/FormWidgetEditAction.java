package com.fr.design.designer.beans.actions;

import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.designer.beans.actions.behavior.UpdateBehavior;
import com.fr.design.mainframe.FormDesigner;
import com.fr.intelli.record.Focus;
import com.fr.intelli.record.Original;
import com.fr.record.analyzer.EnableMetrics;

import javax.swing.JButton;
import java.awt.event.ActionEvent;

@EnableMetrics
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
        if (evt.getSource() instanceof JButton) {
            recordFunction4Toolbar();
        } else {
            recordFunction4PopupMenu();
        }
    }

    @Focus(id = "com.fr.form.widget_edit_toolbar", text = "Fine-Design_Function_Form_Widget_Edit_Toolbar", source = Original.EMBED)
    private void recordFunction4Toolbar() {
        // do nothing
    }

    @Focus(id = "com.fr.form.widget_edit_popupmenu", text = "Fine-Design_Function_Form_Widget_Edit_Popup_Menu", source = Original.EMBED)
    private void recordFunction4PopupMenu() {
        // do nothing
    }

    void setUpdateBehavior(UpdateBehavior updateBehavior) {
        this.updateBehavior = updateBehavior;
    }

}