package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6r
 * Date   : 11-11-30
 * Time   : 上午9:59
 * 柱形图中的条件属性界面
 */
public class BarPlotDataSeriesConditionPane extends DataSeriesConditionPane {
	private static final long serialVersionUID = 7887246749323167953L;
	
	protected void addBorderAction() {
        classPaneMap.put(AttrBorder.class, new LabelBorderPane(this));
    }

    public Class<? extends Plot> class4Correspond() {
        return Bar2DPlot.class;
    }
}