package com.fr.design.gui.core;

import javax.swing.Icon;

import com.fr.base.FRContext;
import com.fr.form.ui.Widget;

public class CustomWidgetOption extends WidgetOption {
	private static final long serialVersionUID = -8144214820100962842L;
	private String optionName;
	private Icon optionIcon;
	private Class<? extends Widget> widgetClass;
	
	public CustomWidgetOption(String optionName, Icon optionIcon, Class<? extends Widget> widgetClass) {
		this.optionName = optionName;
		this.optionIcon = optionIcon;
		this.widgetClass = widgetClass;
	}
	@Override
	public Widget createWidget() {
		Class<? extends Widget> cls = widgetClass();
		try {
			Widget ins = cls.newInstance();
			return ins ;
		} catch (InstantiationException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}

		return null;
	}
	@Override
	public String optionName() {
		return this.optionName;
	}

	@Override
	public Icon optionIcon() {
		return this.optionIcon;
	}

	@Override
	public Class<? extends Widget> widgetClass() {
		return this.widgetClass;
	}

}