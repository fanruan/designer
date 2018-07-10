/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ContainerEvent;
import java.util.Iterator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRGridLayoutAdapter;
import com.fr.design.form.layout.FRGridLayout;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WGridLayout;

/**
 * @author richer
 * @since 6.5.3
 */
public class XWGridLayout extends XLayoutContainer {

    public XWGridLayout(WGridLayout widget, Dimension initSize) {
        super(widget, initSize);
    }
    
    @Override
    protected String getIconName() {
        return "layout_grid.png";
    }
    
    @Override
	public String createDefaultName() {
    	return "grid";
    }

    @Override
    public WGridLayout toData() {
        return (WGridLayout) data;
    }

    @Override
	protected void initLayoutManager() {
        this.setLayout(new FRGridLayout(toData().getRows(), toData().getColumns(), toData().getHgap(), toData().getVgap()));
    }

    @Override
    public void convert() {
        isRefreshing = true;
        WGridLayout layout = this.toData();
        this.removeAll();
        Iterator it = layout.iterator();
        while(it.hasNext()) {
            Point p = (Point)it.next();
            Widget wgt = layout.getWidget(p);
            if (wgt != null) {
                XWidgetCreator comp = (XWidgetCreator)XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
                this.add(comp, p);
            }
        }
        this.repaint();
        isRefreshing = false;
    }

    @Override
    public void componentAdded(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        XWidgetCreator creator = (XWidgetCreator) e.getChild();
        FRGridLayout g = (FRGridLayout) getLayout();
        Point p = g.getPoint(creator);
        WGridLayout wg = this.toData();
        Widget w = creator.toData();
        wg.addWidget(w, p);
    }

	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRGridLayoutAdapter(this);
	}
}