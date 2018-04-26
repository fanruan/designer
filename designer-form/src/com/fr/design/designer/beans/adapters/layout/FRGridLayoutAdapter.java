package com.fr.design.designer.beans.adapters.layout;


import java.awt.Point;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRGridLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.properties.FRGridLayoutPropertiesGroupModel;
import com.fr.design.form.layout.FRGridLayout;
import com.fr.design.utils.gui.LayoutUtils;

public class FRGridLayoutAdapter extends AbstractLayoutAdapter {

    public FRGridLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    @Override
    public boolean accept(XCreator creator, int x, int y) {
        return true;
    }

    @Override
    protected void addComp(XCreator creator, int x, int y) {
        container.add(creator, getLayoutGrid(creator, x, y));
        LayoutUtils.layoutRootContainer(container);
    }

    @Override
    public HoverPainter getPainter() {
        return new FRGridLayoutPainter(container);
    }

    @Override
    public GroupModel getLayoutProperties() {
        return new FRGridLayoutPropertiesGroupModel(container);
    }

    private Point getLayoutGrid(XCreator creator, int x, int y) {
        FRGridLayout layout = (FRGridLayout)container.getLayout();
        int w = container.getWidth() / layout.getColumns();
        int h = container.getHeight() / layout.getRows();
        return new Point(x / w, y / h);
    }
}