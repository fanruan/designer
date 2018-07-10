package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrColor;
import com.fr.chart.chartattr.PiePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelColorPane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 上午9:59
 * 三维饼图的条件属性界面
 */
public class Pie3DPlotDataSeriesConditionPane extends DataSeriesConditionPane {
	private static final long serialVersionUID = 4137513003721586526L;

    protected void addStyleAction() {
        classPaneMap.put(AttrColor.class, new LabelColorPane(this));
    }

    public Class<? extends Plot> class4Correspond() {
        return PiePlot.class;
    }
}