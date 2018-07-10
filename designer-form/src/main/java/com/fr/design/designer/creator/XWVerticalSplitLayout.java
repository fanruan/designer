package com.fr.design.designer.creator;

import java.awt.Dimension;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRVerticalSplitLayoutAdapter;
import com.fr.design.form.layout.FRVerticalSplitLayout;
import com.fr.form.ui.container.WVerticalSplitLayout;

public class XWVerticalSplitLayout extends XAbstractSplitLayout {

	public XWVerticalSplitLayout(WVerticalSplitLayout widget, Dimension initSize) {
		super(widget, initSize);
	}

	@Override
	public WVerticalSplitLayout toData() {
		return (WVerticalSplitLayout) data;
	}

	@Override
	protected void initLayoutManager() {
		this.setLayout(new FRVerticalSplitLayout(toData().getRatio(), toData().getHgap(), toData().getVgap()));
	}

	@Override
	protected String getIconName() {
		return "separator_16.png";
	}

	@Override
	public String createDefaultName() {
		return "vsplit";
	}

	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRVerticalSplitLayoutAdapter(this);
	}
}