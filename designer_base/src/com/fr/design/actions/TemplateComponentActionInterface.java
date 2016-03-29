package com.fr.design.actions;

import com.fr.design.designer.TargetComponent;

public interface TemplateComponentActionInterface<T extends TargetComponent> {
	public T getEditingComponent();
}