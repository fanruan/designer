/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.properties;

import java.awt.Component;

import com.fr.general.Inter;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWHorizontalBoxLayout;
import com.fr.form.ui.container.WHorizontalBoxLayout;

/**
 * @author richer
 * @since 6.5.3
 */
public class HorizontalLayoutConstraints extends HVLayoutConstraints{
	WHorizontalBoxLayout layout;
	
	public HorizontalLayoutConstraints(XLayoutContainer parent, Component comp) {
		super(parent, comp);
		this.layout = ((XWHorizontalBoxLayout) parent).toData();
        this.editor1 = new LayoutConstraintsEditor(layout);
	}

	@Override
    public Object getValue(int row, int column) {
        if (column == 0) {
            switch (row) {
                case 0:
                    return Inter.getLocText("Layout-Index");
                default:
                    return Inter.getLocText("Tree-Width");
            }
        } else {
            switch (row) {
                case 0:
                    return layout.getWidgetIndex(widget) + 1;
                default:
                    return layout.getWidthAtWidget(widget);
            }
        }
    }

    @Override
	public boolean setValue(Object value, int row, int column) {
		if (column == 1) {
			XWHorizontalBoxLayout wLayout = (XWHorizontalBoxLayout) parent;
			switch (row) {
			case 0:
				layout.setWidgetIndex(widget, value == null ? 0 : (((Number) value).intValue() - 1));
				wLayout.convert();
				//TODO:
				return true;
			default:
				layout.setWidthAtWidget(widget, value == null ? 0 : ((Number) value).intValue());
				wLayout.recalculateChildrenPreferredSize();
				return true;
			}
		}
		return true;
	}
}