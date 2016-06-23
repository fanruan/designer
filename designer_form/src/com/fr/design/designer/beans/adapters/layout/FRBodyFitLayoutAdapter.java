package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.properties.BodyMobileLayoutPropertiesGroupModel;

public class FRBodyFitLayoutAdapter extends FRFitLayoutAdapter {

	public FRBodyFitLayoutAdapter(XLayoutContainer container) {
		super(container);
	}
	/**
	 * 返回布局自身属性，方便一些特有设置在layout刷新时处理
	 */
	@Override
    public GroupModel getLayoutProperties() {
		XWFitLayout xfl = (XWFitLayout) container;
        return new BodyMobileLayoutPropertiesGroupModel(xfl);
    }
}