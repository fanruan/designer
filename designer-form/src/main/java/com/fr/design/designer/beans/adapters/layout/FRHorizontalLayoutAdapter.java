package com.fr.design.designer.beans.adapters.layout;


import java.awt.Dimension;
import java.awt.Rectangle;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRHorizontalLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWHorizontalBoxLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.designer.properties.HorizontalLayoutConstraints;
import com.fr.design.designer.properties.HorizontalLayoutPropertiesGroupModel;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WHorizontalBoxLayout;
import com.fr.design.utils.gui.LayoutUtils;

public class FRHorizontalLayoutAdapter extends AbstractLayoutAdapter {
    public FRHorizontalLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    @Override
    public boolean accept(XCreator creator, int x, int y) {
        return true;
    }

    @Override
    public void addComp(XCreator creator, int x, int y) {
    	if(whetherUseBackupSize(creator)) {
    		creator.useBackupSize();
    	}
        container.add(creator, getPlaceIndex(x));
        LayoutUtils.layoutRootContainer(container);
    }
    
    @Override
	public boolean supportBackupSize() {
    	return true;
    }

    private int getPlaceIndex(int x) {
        int place = -1;
        int count = container.getComponentCount();
        for (int i = 0; i < count; i ++) {
            Rectangle bounds = container.getComponent(i).getBounds();
            if (x < bounds.x) {
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
        WHorizontalBoxLayout layout = ((XWHorizontalBoxLayout)container).toData();
        Widget widget = ((XWidgetCreator)creator).toData();
        creator.setPreferredSize(new Dimension(creator.getWidth(),0));
        layout.setWidthAtWidget(widget, creator.getWidth());
    }
    
    @Override
    public HoverPainter getPainter() {
        return new FRHorizontalLayoutPainter(container);
    }

    @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
        return new HorizontalLayoutConstraints(container, creator);
    }
    
    @Override
    public GroupModel getLayoutProperties() {
        return new HorizontalLayoutPropertiesGroupModel((XWHorizontalBoxLayout) container);
    }
}