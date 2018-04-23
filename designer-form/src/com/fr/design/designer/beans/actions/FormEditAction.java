package com.fr.design.designer.beans.actions;

import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.mainframe.FormDesigner;

public abstract class FormEditAction extends TemplateComponentAction<FormDesigner> {

	protected FormEditAction(FormDesigner t) {
		super(t);
	}

	@Override
	public void update() {
		this.setEnabled(true);
	}

}