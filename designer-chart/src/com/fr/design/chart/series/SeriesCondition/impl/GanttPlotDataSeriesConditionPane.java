package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrColor;
import com.fr.chart.chartattr.GanttPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAlphaPane;
import com.fr.design.chart.series.SeriesCondition.LabelColorPane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 上午9:56
 */
public class GanttPlotDataSeriesConditionPane extends DataSeriesConditionPane {

	private static final long serialVersionUID = 2558855009849187262L;
	
	protected void addBasicAction() {
		classPaneMap.put(AttrAlpha.class, new LabelAlphaPane(this));
	}

	protected void addStyleAction() {
         classPaneMap.put(AttrColor.class, new LabelColorPane(this));
    }

    protected void addBorderAction() {

    }

    protected void addTrendLineAction() {

    }

    public Class<? extends Plot>  class4Correspond() {
        return GanttPlot.class;
    }
}