/*
 * Copyright(c) 2001-2010, FineReport  Inc, All Rights Reserved.
 */
package com.fr.grid;

import java.awt.Dimension;

import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.GridUIProcessor;
import com.fr.design.mainframe.ElementCasePane;

import javax.swing.plaf.ComponentUI;

/**
 * GridRow used to paint and edit grid row.
 * 
 * @editor zhou
 * @since 2012-3-22下午6:12:03
 */
public class GridRow extends GridHeader<Integer> {

	private static final int MAX = 5;
	private int resolution = ScreenResolution.getScreenResolution();

	@Override
	protected void initByConstructor() {
		GridRowMouseHandler gridRowMouseHandler = new GridRowMouseHandler(this);
		this.addMouseListener(gridRowMouseHandler);
		this.addMouseMotionListener(gridRowMouseHandler);
		this.updateUI();
	}

	@Override
	public Integer getDisplay(int index) {
		return new Integer(index + 1);
	}

	@Override
	public void updateUI() {
		this.setUI(new GridRowUI(resolution));
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}


	/**
	 * Gets the preferred size.
	 */
	@Override
	public Dimension getPreferredSize() {
		ElementCasePane reportPane = this.getElementCasePane();

		if (!(reportPane.isRowHeaderVisible())) {
			return new Dimension(0, 0);
		}

		int maxCharNumber = this.caculateMaxCharNumber(reportPane);
		return new Dimension(maxCharNumber * GraphHelper.getFontMetrics(this.getFont()).charWidth('M'), super.getPreferredSize().height);
	}

	/**
	 * Calculates max char number.
	 */
	private int caculateMaxCharNumber(ElementCasePane reportPane) {
		int maxCharNumber = MAX;
		maxCharNumber = Math.max(maxCharNumber, ("" + (reportPane.getGrid().getVerticalValue() + reportPane.getGrid().getVerticalExtent())).length() + 1);

		return maxCharNumber;
	}
}