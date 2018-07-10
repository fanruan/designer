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

	private static final int MAX = 4;
	private int resolution = ScreenResolution.getScreenResolution();
	private GridRowMouseHandler gridRowMouseHandler;

	@Override
	protected void initByConstructor() {
		resolution = ScreenResolution.getScreenResolution();
		this.setResolution(resolution);
		gridRowMouseHandler = new GridRowMouseHandler(this);
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
		this.removeMouseListener(gridRowMouseHandler);
		this.removeMouseMotionListener(gridRowMouseHandler);
		gridRowMouseHandler = new GridRowMouseHandler(this);
		this.addMouseListener(gridRowMouseHandler);
		this.addMouseMotionListener(gridRowMouseHandler);
		this.setUI(new GridRowUI(resolution));
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	public int getResolution() {
		return this.resolution;
	}


	/**
	 * Gets the preferred size.
	 */
	@Override
	public Dimension getPreferredSize() {
		ElementCasePane reportPane = this.getElementCasePane();
		float time = (float)reportPane.getResolution()/ ScreenResolution.getScreenResolution();
		if (!(reportPane.isRowHeaderVisible())) {
			return new Dimension(0, 0);
		}

		int maxCharNumber = this.caculateMaxCharNumber(reportPane);
		return new Dimension((int) (maxCharNumber * GraphHelper.getFontMetrics(this.getFont()).charWidth('M') * time), super.getPreferredSize().height);
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