package com.fr.design.actions.file;

import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.TemplatePane;

import java.awt.event.ActionEvent;



public class EditEnvAction extends UpdateAction {


    public EditEnvAction() {
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_M_Others") + "...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	TemplatePane.getInstance().editItems();

    }
}