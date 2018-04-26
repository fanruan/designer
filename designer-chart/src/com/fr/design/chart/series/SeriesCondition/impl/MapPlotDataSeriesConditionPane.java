package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrContents;
import com.fr.chart.chartattr.MapPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelContentsPane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-1
 * Time   : 上午9:32
 * 地图 条件属性界面
 */
public class MapPlotDataSeriesConditionPane extends DataSeriesConditionPane {
	private static final long serialVersionUID = 3698292568618077981L;

    protected void addBasicAction() {
        classPaneMap.put(AttrContents.class, new LabelContentsPane(this, class4Correspond()));
    }
    
    /**
     * 返回界面对应的class
     * @return  返回界面对应class
     */
	public Class<? extends Plot> class4Correspond() {
        return MapPlot.class;
    }
}