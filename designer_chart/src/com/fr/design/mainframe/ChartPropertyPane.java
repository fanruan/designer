/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.fr.design.gui.ilable.UILabel;

public class ChartPropertyPane extends MiddleChartPropertyPane{

	/**
	 * 创建图表属性表实例.
	 */
	public synchronized static ChartPropertyPane getInstance() {
		if(singleton == null) {
			singleton = new ChartPropertyPane();
		}
		
		singleton.setSureProperty();
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
	
	@Override
	public void setWidgetPropertyPane(BaseWidgetPropertyPane pane) {
		
	}
}