package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteBodyLayout;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.properties.FRAbsoluteBodyLayoutPropertiesGroupModel;
import com.fr.design.designer.properties.FRAbsoluteLayoutPropertiesGroupModel;
import com.fr.form.ui.container.WBodyLayoutType;

/**
 * Created by zhouping on 2016/10/14.
 */
public class FRAbsoluteBodyLayoutAdapter extends FRAbsoluteLayoutAdapter {
    public FRAbsoluteBodyLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    @Override
    public GroupModel getLayoutProperties() {
        XWAbsoluteBodyLayout xwAbsoluteBodyLayout = (XWAbsoluteBodyLayout) container;
        //如果body是绝对布局，那么获取原来自适应body的属性--布局类型
        WBodyLayoutType layoutType = WBodyLayoutType.FIT;
        if (container.getParent() != null) {
            layoutType = ((XWFitLayout)container.getParent()).toData().getBodyLayoutType();
        }
        return new FRAbsoluteBodyLayoutPropertiesGroupModel(xwAbsoluteBodyLayout, layoutType);
    }
}
