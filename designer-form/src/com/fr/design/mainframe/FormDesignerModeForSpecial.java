package com.fr.design.mainframe;

import java.util.ArrayList;

import com.fr.design.gui.itable.PropertyGroup;
import com.fr.design.gui.core.WidgetOption;

public abstract class FormDesignerModeForSpecial<T> {

	private T target;

	public FormDesignerModeForSpecial(T t) {
		this.target = t;
	}

	public T getTarget() {
		return target;
	}

	public abstract WidgetOption[] getPredefinedWidgetOptions();

	public abstract ArrayList<PropertyGroup> createRootDesignerPropertyGroup();

	public abstract boolean isFormParameterEditor();
	
	public abstract int getMinDesignWidth();
	
	public abstract int getMinDesignHeight();
}