package com.fr.design.designer.creator;

import java.awt.Dimension;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRHorizontalSplitLayoutAdapter;
import com.fr.design.form.layout.FRHorizontalSplitLayout;
import com.fr.form.ui.container.WHorizontalSplitLayout;

public class XWHorizontalSplitLayout extends XAbstractSplitLayout {

	public XWHorizontalSplitLayout(WHorizontalSplitLayout widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	@Override
	public WHorizontalSplitLayout toData() {
		return (WHorizontalSplitLayout)data;
	}
	
	@Override
	protected void initLayoutManager() {
		this.setLayout(new FRHorizontalSplitLayout(toData().getRatio(), toData().getHgap(), toData().getVgap()));
	}

	@Override
	protected String getIconName() {
		return "split_pane_16.png";
	}
	
	@Override
	public String createDefaultName() {
    	return "hsplit";
    }

	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRHorizontalSplitLayoutAdapter(this);
	}
}