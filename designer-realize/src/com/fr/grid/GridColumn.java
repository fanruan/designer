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
import com.fr.design.mainframe.JSliderPane;
import com.fr.stable.StableUtils;

import javax.swing.plaf.ComponentUI;

/**
 * GridColumn used to paint and edit grid column.
 *
 * @editor zhou
 * @since 2012-3-22下午6:12:40
 */
public class GridColumn extends GridHeader<String> {

	public int resolution = ScreenResolution.getScreenResolution();

	private GridColumnMouseHandler gridColumnMouseHandler;

	@Override
	protected void initByConstructor() {
		resolution = ScreenResolution.getScreenResolution();
		this.setResolution(resolution);
		gridColumnMouseHandler = new GridColumnMouseHandler(this);
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
		this.removeMouseListener(gridColumnMouseHandler);
		this.removeMouseMotionListener(gridColumnMouseHandler);
		gridColumnMouseHandler = new GridColumnMouseHandler(this);
		this.addMouseListener(gridColumnMouseHandler);
		this.addMouseMotionListener(gridColumnMouseHandler);
//		gridColumnMouseHandler.setResolution(resolution);
		this.setUI(new GridColumnUI(resolution));
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	@Override
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
		if (!reportPane.isColumnHeaderVisible()) {
			return new Dimension(0, 0);
		}

		return new Dimension(super.getPreferredSize().width, (int) (GraphHelper.getFontMetrics(this.getFont()).getHeight() * time + SIZE_ADJUST));
	}
}