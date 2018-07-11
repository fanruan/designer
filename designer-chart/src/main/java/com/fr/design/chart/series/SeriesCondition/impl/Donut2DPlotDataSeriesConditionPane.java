package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartattr.Donut2DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;

public class Donut2DPlotDataSeriesConditionPane extends DataSeriesConditionPane{
	private static final long serialVersionUID = -8816067568992838526L;
	
	protected void addBorderAction() {
        classPaneMap.put(AttrBorder.class, new LabelBorderPane(this));
    }

    public Class<? extends Plot> class4Correspond() {
        return Donut2DPlot.class;
    }

}