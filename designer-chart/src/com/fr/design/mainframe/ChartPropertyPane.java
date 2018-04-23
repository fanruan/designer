/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;
import java.awt.*;

public class ChartPropertyPane extends MiddleChartPropertyPane{

	/**
	 * 创建图表属性表实例.
	 */
	private synchronized static ChartPropertyPane getInstance() {
		//todo
		//创建新图表时，创建属性表配置面板
		singleton = new ChartPropertyPane();
		return singleton;
	}

	private static ChartPropertyPane singleton;

	@Override
	protected void createNameLabel() {
		nameLabel = new UILabel() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 18);
			}
		};
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}

	@Override
	protected void createMainPane() {
		this.add(chartEditPane, BorderLayout.CENTER);
	}

	@Override
	protected JComponent createNorthComponent() {
		return nameLabel;
	}

	public synchronized static void  clear() {
		singleton =  null;
	}
}