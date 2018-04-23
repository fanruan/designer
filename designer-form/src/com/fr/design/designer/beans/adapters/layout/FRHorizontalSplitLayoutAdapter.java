package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWHorizontalSplitLayout;
import com.fr.design.form.layout.FRSplitLayout;
import com.fr.form.ui.container.WHorizontalSplitLayout;

public class FRHorizontalSplitLayoutAdapter extends FRVerticalSplitLayoutAdapter {

	public FRHorizontalSplitLayoutAdapter(XLayoutContainer container) {
		super(container);
	}

	@Override
	protected String getPlacement(XCreator creator, int x, int y) {
		int width = container.getWidth();
		WHorizontalSplitLayout wLayout = ((XWHorizontalSplitLayout) container).toData();
		int asideSize = (int) (width * wLayout.getRatio());
		if (x > asideSize) {
			return FRSplitLayout.CENTER;
		} else {
			return FRSplitLayout.ASIDE;
		}
	}
}