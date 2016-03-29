/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRHorizontalLayoutAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.form.layout.FRHorizontalLayout;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WHorizontalBoxLayout;

/**
 * @author richer
 * @since 6.5.3
 */
public class XWHorizontalBoxLayout extends XLayoutContainer {

    public XWHorizontalBoxLayout(WHorizontalBoxLayout widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    protected String getIconName() {
        return "boxlayout_h_16.png";
    }
    
    @Override
	public String createDefaultName() {
    	return "hBox";
    }
    
	@Override
	public Dimension initEditorSize() {
		return new Dimension(200, 100);
	}
    
    @Override
    public WHorizontalBoxLayout toData() {
        return (WHorizontalBoxLayout) data;
    }

    @Override
	protected void initLayoutManager() {
        this.setLayout(new FRHorizontalLayout(toData().getAlignment(), toData().getHgap(), toData().getVgap()));
    }

    @Override
    public void componentAdded(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        XWidgetCreator creator = (XWidgetCreator) e.getChild();
        WHorizontalBoxLayout wlayout = this.toData();
        Widget wgt = creator.toData();
        for (int i = 0, count = this.getComponentCount(); i < count; i++) {
            if (creator == this.getComponent(i)) {
                wlayout.addWidget(wgt, i);
                wlayout.setWidthAtWidget(wgt, creator.getWidth());
            }
        }
        this.recalculateChildrenPreferredSize();
    }

    @Override
    protected Dimension calculatePreferredSize(Widget wgt) {
        // 注意这里计算PreferredSize的时候需要取当前容器的实际大小
        // 高度是自适应的，直接就写成0了
        return new Dimension(this.toData().getWidthAtWidget(wgt), 0);
    }

    // 在添加的时候需要把可拉伸的方向确定，所以重写了add方法
    @Override
    public Component add(Component comp, int index) {
        super.add(comp, index);
        if (comp == null) {
            return null;
        }
        XCreator creator = (XCreator) comp;
        creator.setDirections(new int[]{Direction.LEFT, Direction.RIGHT});
        return comp;
    }

	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRHorizontalLayoutAdapter(this);
	}
}