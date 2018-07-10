package com.fr.design.actions;

import com.fr.design.mainframe.ReportComponent;

public abstract class ReportComponentAction<T extends ReportComponent> extends TemplateComponentAction<T> {
	protected ReportComponentAction(T tc) {
		super(tc);
	}
}