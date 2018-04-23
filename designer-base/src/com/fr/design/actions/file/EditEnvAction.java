package com.fr.design.actions.file;

import java.awt.event.ActionEvent;

import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.TemplatePane;
import com.fr.general.Inter;


public class EditEnvAction extends UpdateAction {


    public EditEnvAction() {
        this.setName(Inter.getLocText("M-Others") + "...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	TemplatePane.getInstance().editItems();

    }
}