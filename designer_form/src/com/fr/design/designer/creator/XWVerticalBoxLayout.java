/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRVerticalLayoutAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.form.layout.FRVerticalLayout;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WVerticalBoxLayout;


/**
 * @author richer
 * @since 6.5.3
 */
public class XWVerticalBoxLayout extends XLayoutContainer {

    public XWVerticalBoxLayout(WVerticalBoxLayout widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    public WVerticalBoxLayout toData() {
        return (WVerticalBoxLayout) data;
    }

	@Override
	public Dimension initEditorSize() {
		return new Dimension(100, 200);
	}

    @Override
	protected void initLayoutManager() {
        this.setLayout(new FRVerticalLayout(this.toData().getHgap(), this.toData().getVgap()));
    }

     @Override
    public void componentAdded(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        XWidgetCreator creator = (XWidgetCreator) e.getChild();
        WVerticalBoxLayout wlayout = this.toData();
        Widget wgt = creator.toData();
        for (int i = 0, count = this.getComponentCount(); i < count; i++) {
            if (creator == this.getComponent(i)) {
                wlayout.addWidget(wgt, i);
                wlayout.setHeightAtWidget(wgt, creator.getHeight());
            }
        }
        this.recalculateChildrenPreferredSize();
    }

    @Override
    protected Dimension calculatePreferredSize(Widget wgt) {
        // 注意这里计算PreferredSize的时候需要取当前容器的实际大小
        return new Dimension(this.getSize().width, this.toData().getHeightAtWidget(wgt));
    }

    @Override
    protected String getIconName() {
        return "boxlayout_v_16.png";
    }
    
    @Override
	public String createDefaultName() {
    	return "vBox";
    }
    
    // 在添加的时候需要把可拉伸的方向确定，所以重写了add方法
    @Override
    public Component add(Component comp, int index) {
        super.add(comp, index);
        if (comp == null) {
            return null;
        }
        XCreator creator = (XCreator) comp;
        creator.setDirections(new int[]{Direction.TOP, Direction.BOTTOM});
        return comp;
    }

	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRVerticalLayoutAdapter(this);
	}
}