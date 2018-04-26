package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;

/**
 * 类说明: 组合图中 堆积面积图(无趋势线)条件属性界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-4-20 下午05:28:52
 */
public class CustomPlot4AreaConditionPane extends AreaPlotDataSeriesCondtionPane {
	private static final long serialVersionUID = 6938149100125099651L;

	protected void addAxisPositionAction() {
		classPaneMap.put(AttrAxisPosition.class, new LabelAxisPositionPane(this));
	}
}