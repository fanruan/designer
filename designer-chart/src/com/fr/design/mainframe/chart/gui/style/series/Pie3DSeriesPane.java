package com.fr.design.mainframe.chart.gui.style.series;

import javax.swing.JPanel;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

/**
 * 三维饼图的系列属性界面, 没有风格, 没有第二绘图区.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-10-30 上午09:59:31
 */
public class Pie3DSeriesPane extends Pie2DSeriesPane {

	public Pie3DSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot);
	}
	
	@Override
	protected JPanel getContentInPlotType() {
		initCom();

		return null;
	}

}