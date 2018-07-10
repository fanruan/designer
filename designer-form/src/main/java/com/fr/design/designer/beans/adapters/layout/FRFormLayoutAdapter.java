/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.properties.FRFormLayoutPropertiesGroupModel;
import com.fr.design.utils.gui.LayoutUtils;

/**
 * @author richer
 * @since 6.5.3
 * 表单布局适配器
 */
public class FRFormLayoutAdapter extends AbstractLayoutAdapter {

    public FRFormLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    @Override
    public boolean accept(XCreator creator, int x, int y) {
        return true;
    }

    @Override
    protected void addComp(XCreator creator, int x, int y) {
        int w = creator.getWidth() / 2;
        int h = creator.getHeight() / 2;
        creator.setLocation(x - w, y - h);
        container.add(creator);
        LayoutUtils.layoutRootContainer(container);
    }

    @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
        return null;
    }

    @Override
    public GroupModel getLayoutProperties() {
        return new FRFormLayoutPropertiesGroupModel(container);
    }
}