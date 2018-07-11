package com.fr.design.actions;

import com.fr.design.mainframe.JTemplate;

public abstract class JTemplateAction<T extends JTemplate<?, ?>> extends UpdateAction implements TemplateComponentActionInterface<T> {
	private T t;
	public JTemplateAction(T t) {
		this.t = t;
	}
	
	@Override
	public T getEditingComponent() {
		return t;
	}

}