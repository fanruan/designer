package com.fr.design.designer.beans.adapters.layout;


import java.awt.Dimension;
import java.awt.Rectangle;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRVerticalLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWVerticalBoxLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.designer.properties.VerticalBoxProperties;
import com.fr.design.designer.properties.VerticalLayoutConstraints;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WVerticalBoxLayout;
import com.fr.design.utils.gui.LayoutUtils;

public class FRVerticalLayoutAdapter extends AbstractLayoutAdapter {

    public FRVerticalLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    @Override
    public boolean accept(XCreator creator, int x, int y) {
        return true;
    }

    @Override
    protected void addComp(XCreator creator, int x, int y) {
    	if(whetherUseBackupSize(creator)) {
    		creator.useBackupSize();
    	}
        container.add(creator, getPlaceIndex(y));
        LayoutUtils.layoutRootContainer(container);
    }
    
    @Override
	public boolean supportBackupSize() {
    	return true;
    }

    private int getPlaceIndex(int y) {
        int place = -1;
        int count = container.getComponentCount();
        for (int i = 0; i < count; i++) {
            Rectangle bounds = container.getComponent(i).getBounds();
            if (y < bounds.y) {
                return i;
            }
        }
        if (place == -1) {
            return count;
        }
        return place;
    }

     @Override
	public void fix(XCreator creator) {
		WVerticalBoxLayout layout = ((XWVerticalBoxLayout) container).toData();
		Widget widget = ((XWidgetCreator) creator).toData();
		creator.setPreferredSize(new Dimension(0, creator.getHeight()));
		layout.setHeightAtWidget(widget, creator.getHeight());
	}

    @Override
    public HoverPainter getPainter() {
        return new FRVerticalLayoutPainter(container);
    }

    @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
        return new VerticalLayoutConstraints(container, creator);
    }

    @Override
    public GroupModel getLayoutProperties() {
        XWVerticalBoxLayout xbl = (XWVerticalBoxLayout) container;
        return new VerticalBoxProperties(xbl);
    }
}