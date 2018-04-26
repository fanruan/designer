package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.chartattr.AreaPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 上午10:00
 * 面积图的条件属性界面 
 */
public class AreaPlotDataSeriesCondtionPane extends DataSeriesConditionPane {
	private static final long serialVersionUID = -7003756827950382930L;

    /**
     *  返回对应的class
     * @return 返回对应的class
     */
    public Class<? extends Plot> class4Correspond() {
        return AreaPlot.class;
    }
}