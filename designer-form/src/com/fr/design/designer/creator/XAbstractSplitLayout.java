package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.awt.event.ContainerEvent;

import com.fr.design.form.layout.FRSplitLayout;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WSplitLayout;

public abstract class XAbstractSplitLayout extends XLayoutContainer {

	public XAbstractSplitLayout(WSplitLayout widget, Dimension initSize) {
		super(widget, initSize);
	}

	@Override
	public WSplitLayout toData() {
		return (WSplitLayout) data;
	}

	@Override
	public void convert() {
		isRefreshing = true;
		WSplitLayout wb = this.toData();
		this.removeAll();
		String[] arrs = { WSplitLayout.CENTER, WSplitLayout.ASIDE };
		for (int i = 0; i < arrs.length; i++) {
			Widget wgt = wb.getLayoutWidget(arrs[i]);
			if (wgt != null) {
				XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
				this.add(comp, arrs[i]);
			}
		}
		isRefreshing = false;
	}

	@Override
	public void componentAdded(ContainerEvent e) {
		if (isRefreshing) {
			return;
		}
		XWidgetCreator creator = (XWidgetCreator) e.getChild();
		FRSplitLayout b = (FRSplitLayout) getLayout();
		Object constraints = b.getConstraints(creator);
		WSplitLayout wb = this.toData();
		Widget w = creator.toData();
		wb.addWidget(w, constraints);
	}
}