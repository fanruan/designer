package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartattr.PiePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 上午10:01
 * 饼图 条件属性界面
 */

public class PiePlotDataSeriesConditionPane extends DataSeriesConditionPane {
	private static final long serialVersionUID = 7442193860514835962L;

	protected void addBorderAction() {
        classPaneMap.put(AttrBorder.class, new LabelBorderPane(this));
    }

    public Class<? extends Plot> class4Correspond() {
        return PiePlot.class;
    }
}