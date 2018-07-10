package com.fr.design.designer.beans.adapters.layout;

import java.awt.Component;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XAbstractSplitLayout;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWVerticalSplitLayout;
import com.fr.design.designer.properties.HorizontalSplitProperties;
import com.fr.design.form.layout.FRSplitLayout;
import com.fr.form.ui.container.WVerticalSplitLayout;
import com.fr.design.utils.gui.LayoutUtils;

public class FRVerticalSplitLayoutAdapter extends AbstractLayoutAdapter {

	public FRVerticalSplitLayoutAdapter(XLayoutContainer container) {
		super(container);
	}

	@Override
	public boolean accept(XCreator creator, int x, int y) {
		String place = getPlacement(creator, x, y);
		FRSplitLayout layout = (FRSplitLayout) container.getLayout();
		Component comp = layout.getLayoutComponent(place);
		return comp == null;
	}

	@Override
	protected void addComp(XCreator creator, int x, int y) {
		String placement = getPlacement(creator, x, y);
		container.add(creator, placement);
		LayoutUtils.layoutRootContainer(container);
	}

	@Override
	public GroupModel getLayoutProperties() {
		XAbstractSplitLayout xbl = (XAbstractSplitLayout) container;
		return new HorizontalSplitProperties(xbl.toData());
	}

	protected String getPlacement(XCreator creator, int x, int y) {
		int height = container.getHeight();
		WVerticalSplitLayout wLayout = ((XWVerticalSplitLayout) container).toData();
		int asideSize = (int) (height * wLayout.getRatio());
		if (y > asideSize) {
			return FRSplitLayout.CENTER;
		} else {
			return FRSplitLayout.ASIDE;
		}
	}
}