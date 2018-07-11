package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;

/**
 * 类说明: 组合图中 柱形的条件属性界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-4-20 下午05:14:44
 */
public class CustomPlot4BarNoTrendLineConditionPane extends BarPlotDataSeriesConditionPane {
	private static final long serialVersionUID = -6960758805042551364L;

	protected void addAxisPositionAction() {
		classPaneMap.put(AttrAxisPosition.class, new LabelAxisPositionPane(this));
	}
}