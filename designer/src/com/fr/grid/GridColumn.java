/*
 * Copyright(c) 2001-2010, FineReport  Inc, All Rights Reserved.
 */
package com.fr.grid;

import java.awt.Dimension;

import com.fr.base.GraphHelper;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.stable.StableUtils;

/**
 * GridColumn used to paint and edit grid column.
 *
 * @editor zhou
 * @since 2012-3-22下午6:12:40
 */
public class GridColumn extends GridHeader<String> {

	@Override
	protected void initByConstructor() {
		GridColumnMouseHandler gridColumnMouseHandler = new GridColumnMouseHandler(this);
		this.addMouseListener(gridColumnMouseHandler);
		this.addMouseMotionListener(gridColumnMouseHandler);
		this.updateUI();
	}

	@Override
	public String getDisplay(int index) {
		return StableUtils.convertIntToABC(index + 1);
	}

	@Override
	public void updateUI() {
		this.setUI(new GridColumnUI());
	}

	/**
	 * Gets the preferred size.
	 */
	@Override
	public Dimension getPreferredSize() {
		ElementCasePane reportPane = this.getElementCasePane();

		if (!reportPane.isColumnHeaderVisible()) {
			return new Dimension(0, 0);
		}

		return new Dimension(super.getPreferredSize().width, GraphHelper.getFontMetrics(this.getFont()).getHeight() + SIZE_ADJUST);
	}
}