package com.fr.design.mainframe.chart.gui.style;

import com.fr.design.gui.style.GradientPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundPane;

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
		paneList.add(new NullBackgroundPane());
		paneList.add(new ColorBackgroundPane());
		paneList.add(new GradientPane(CHART_GRADIENT_WIDTH));
	}
}