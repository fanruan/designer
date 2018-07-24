package com.fr.design.designer.properties;


import com.fr.form.ui.container.WSplitLayout;

public class HorizontalSplitProperties extends VerticalSplitProperties {
	public HorizontalSplitProperties(WSplitLayout wLayout) {
		super(wLayout);
	}

	@Override
	public String getGroupName() {
		return com.fr.design.i18n.Toolkit.i18nText("Horizontal-Split_Layout");
	}
}