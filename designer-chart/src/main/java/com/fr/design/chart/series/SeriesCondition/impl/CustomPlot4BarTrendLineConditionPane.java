package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;

/**
 * 类说明: 组合图中 非堆积 带趋势线的条件属性界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-4-20 下午05:23:38
 */
public class CustomPlot4BarTrendLineConditionPane extends Bar2DTrendLineDSConditionPane {
	private static final long serialVersionUID = 5216036756293601852L;

	protected void addAxisPositionAction() {
		classPaneMap.put(AttrAxisPosition.class, new LabelAxisPositionPane(this));
	}
}