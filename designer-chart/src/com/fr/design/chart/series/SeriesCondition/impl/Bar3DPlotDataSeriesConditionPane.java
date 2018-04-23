package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.chartattr.Bar3DPlot;
import com.fr.chart.chartattr.Plot;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-1
 * Time   : 上午9:27
 */
public class Bar3DPlotDataSeriesConditionPane extends BarPlotDataSeriesConditionPane {
	private static final long serialVersionUID = -2701123736889274105L;

	public Class<? extends Plot> class4Correspond() {
        return Bar3DPlot.class;
    }
}