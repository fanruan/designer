package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.chartattr.MeterPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-1
 * Time   : 上午9:34
 * 仪表盘 条件属性界面
 */
public class MeterPlotDataSeriesConditionPane extends DataSeriesConditionPane {
	private static final long serialVersionUID = -1476436318592049848L;

	public Class<? extends Plot> class4Correspond() {
        return MeterPlot.class;
    }
}