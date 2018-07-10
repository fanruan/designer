package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.AttrColor;
import com.fr.chart.chartattr.Area3DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;
import com.fr.design.chart.series.SeriesCondition.LabelColorPane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 上午9:58
 * 三维面积图中的条件属性界面
 */
public class Area3DPlotDataSeriesConditionPane extends DataSeriesConditionPane {

	private static final long serialVersionUID = -6553187235124152023L;

    protected void addStyleAction() {
        classPaneMap.put(AttrColor.class, new LabelColorPane(this));
    }

    protected void addBorderAction() {
        classPaneMap.put(AttrBorder.class, new LabelBorderPane(this));
    }

    /**
     * 对应的Class
     * @return 返回对应的class
     */
    public Class<? extends Plot> class4Correspond() {
        return Area3DPlot.class;
    }
}