package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrTrendLine;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.StockPlot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.TrendLinePane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 上午9:56
 * 股价图的条件属性界面
 */
public class StockPlotDataSeriesConditionPane extends DataSeriesConditionPane {
	private static final long serialVersionUID = 1348842506599776627L;

	protected void addTrendLineAction() {
        classPaneMap.put(AttrTrendLine.class, new TrendLinePane(this));
    }

    public Class<? extends Plot> class4Correspond() {
        return StockPlot.class;
    }
}