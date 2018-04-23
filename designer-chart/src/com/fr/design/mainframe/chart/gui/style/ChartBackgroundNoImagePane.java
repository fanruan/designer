package com.fr.design.mainframe.chart.gui.style;

import com.fr.design.mainframe.backgroundpane.ColorBackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.GradientBackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundQuickPane;

/**
 * 背景界面, 无图片和纹理选项.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-21 下午04:00:58
 */
public class ChartBackgroundNoImagePane extends ChartBackgroundPane {

	public ChartBackgroundNoImagePane() {
		super();
	}
	
	protected void initList() {
		paneList.add(new NullBackgroundQuickPane());
		paneList.add(new ColorBackgroundQuickPane());
		paneList.add(new GradientBackgroundQuickPane(CHART_GRADIENT_WIDTH));
	}
}