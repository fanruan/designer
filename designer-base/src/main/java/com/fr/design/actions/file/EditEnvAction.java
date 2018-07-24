package com.fr.design.actions.file;

import java.awt.event.ActionEvent;

import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.TemplatePane;



public class EditEnvAction extends UpdateAction {


    public EditEnvAction() {
        this.setName(com.fr.design.i18n.Toolkit.i18nText("M-Others") + "...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	TemplatePane.getInstance().editItems();

    }
}