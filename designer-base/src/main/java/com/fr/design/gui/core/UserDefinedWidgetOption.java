package com.fr.design.gui.core;

import javax.swing.Icon;

import com.fr.base.BaseUtils;
import com.fr.form.ui.NameWidget;
import com.fr.form.ui.Widget;

public class UserDefinedWidgetOption extends WidgetOption {
	
	private String widgetConfigName;
	private Widget widget;

	public UserDefinedWidgetOption(String name, Widget widget) {
		this.widgetConfigName = name;
		this.widget = widget;
	}

	@Override
	public Widget createWidget() {
		Widget widget = new NameWidget(widgetConfigName);
		widget.setEnabled(this.widget.isEnabled());
		widget.setVisible(this.widget.isVisible());
		return widget;
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