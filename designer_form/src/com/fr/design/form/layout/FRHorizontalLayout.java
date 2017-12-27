/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.form.layout;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRHorizontalLayoutAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWHorizontalBoxLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WHorizontalBoxLayout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ContainerEvent;

/**
 * @author richer
 * @since 6.5.3 水平布局 所有布局内的控件的高度将和容器的高度一样，宽度以及对齐方式可以自由设置
 */
public class FRHorizontalLayout extends FRFlowLayout {

	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;

	public FRHorizontalLayout() {
		this(CENTER);
	}

	public FRHorizontalLayout(int align) {
		super(0, 0);
		setAlignment(align);
	}


	public FRHorizontalLayout(int alignment, int hgap, int vgap) {
		super(alignment,hgap,vgap);
	}

	@Override
	protected void moveComponents(Container target, Insets insets, int total) {
		int width = target.getWidth() - total - insets.left - insets.right;
		int x = insets.left;
		switch (alignment) {
		case LEFT:
			x += 0;
			break;
		case CENTER:
			x += width / 2;
			break;
		case RIGHT:
			x += width;
			break;
		}
		int ey = insets.top + vgap;
		for (int i = 0; i < target.getComponentCount(); i++) {
			Component m = target.getComponent(i);
			if (m.isVisible()) {
				m.setLocation(x, ey);
				x += m.getPreferredSize().width + hgap;
			}
		}

	}

	@Override
	protected int reSizeComponents(Container target, Insets insets) {
		int eachHeight = target.getHeight() - insets.top - insets.bottom - 2 * vgap;
		int total = 0;
		for (int i = 0; i < target.getComponentCount(); i++) {
			Component m = target.getComponent(i);
			if (m.isVisible()) {
				m.setSize(m.getPreferredSize().width, eachHeight);
				total += m.getPreferredSize().width + hgap;
			}
		}
		return total;
	}

	public void componentAdded(ContainerEvent e, WHorizontalBoxLayout wlayout){
		XWidgetCreator creator = (XWidgetCreator) e.getChild();
		Widget wgt = creator.toData();
		wlayout.setWidthAtWidget(wgt, creator.getWidth());
	}

	public Dimension calculatePreferredSize(WHorizontalBoxLayout wlayout, Widget widget){
		return new Dimension(wlayout.getWidthAtWidget(widget), 0);
	}


	public void setDirections(Component component){
		XCreator creator = (XCreator) component;
		creator.setDirections(new int[]{Direction.LEFT, Direction.RIGHT});
	}

	public LayoutAdapter getLayoutAdapter(XWHorizontalBoxLayout xwHorizontalBoxLayout){
		return new FRHorizontalLayoutAdapter(xwHorizontalBoxLayout);
	}

	public void adjustComponents(Component component){

	}

}