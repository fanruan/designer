package com.fr.design.gui.core;

import javax.swing.Icon;

import com.fr.base.BaseUtils;
import com.fr.form.ui.NameWidget;
import com.fr.form.ui.Widget;

public class UserDefinedWidgetOption extends WidgetOption {
	
	private String widgetConfigName;

	public UserDefinedWidgetOption(String name) {
		this.widgetConfigName = name;
	}

	@Override
	public Widget createWidget() {
		return new NameWidget(widgetConfigName);
	}

	@Override
	public Icon optionIcon() {
		return BaseUtils.readIcon("/com/fr/design/images/data/user_widget.png");
	}

	@Override
	public String optionName() {
		return widgetConfigName;
	}

	@Override
	public Class<? extends Widget> widgetClass() {
		return NameWidget.class;
	}
}