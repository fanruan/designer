package com.fr.design.designer.properties;

import com.fr.general.Inter;
import com.fr.form.ui.container.WSplitLayout;

public class HorizontalSplitProperties extends VerticalSplitProperties {
	public HorizontalSplitProperties(WSplitLayout wLayout) {
		super(wLayout);
	}

	@Override
	public String getGroupName() {
		return Inter.getLocText("Horizontal-Split_Layout");
	}
}