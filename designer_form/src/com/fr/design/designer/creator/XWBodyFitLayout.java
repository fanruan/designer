package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRBodyFitLayoutAdapter;
import com.fr.form.ui.container.WFitLayout;
import java.awt.Dimension;

public class XWBodyFitLayout extends XWFitLayout {
	public XWBodyFitLayout() {
		this(new WFitLayout(), new Dimension());
	}

	public XWBodyFitLayout(WFitLayout widget, Dimension initSize) {
		super(widget, initSize);
	}
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRBodyFitLayoutAdapter(this);
	}
}