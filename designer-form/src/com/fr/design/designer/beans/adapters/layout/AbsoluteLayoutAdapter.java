package com.fr.design.designer.beans.adapters.layout;


import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.utils.gui.LayoutUtils;

public class AbsoluteLayoutAdapter extends AbstractLayoutAdapter {

    public AbsoluteLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    @Override
    public boolean accept(XCreator creator, int x, int y) {
        return true;
    }

    @Override
    public void addComp(XCreator creator, int x, int y) {
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
}