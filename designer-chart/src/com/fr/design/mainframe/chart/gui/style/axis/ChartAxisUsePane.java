package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.AbstractChartTabPane;


/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-4 下午05:02:23
 */
public abstract class ChartAxisUsePane<T> extends AbstractChartTabPane<T>{
	
	public abstract void populateBean(Axis axis, Plot plot);
	
	public abstract void updateBean(Axis axis, Plot plot);
}