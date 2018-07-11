package com.fr.design.actions.form;

import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.mainframe.form.FormElementCasePaneDelegate;

public class AbastractFormECAction <T extends FormElementCasePaneDelegate> extends TemplateComponentAction<T>{

	protected AbastractFormECAction(T t) {
		super(t);
	}

	@Override
	public boolean executeActionReturnUndoRecordNeeded() {
		return false;
	}

}