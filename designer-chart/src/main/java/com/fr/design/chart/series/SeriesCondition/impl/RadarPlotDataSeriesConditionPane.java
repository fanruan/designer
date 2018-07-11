package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.AttrMarkerType;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.RadarPlot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelColorPane;
import com.fr.design.chart.series.SeriesCondition.LabelLineStylePane;
import com.fr.design.chart.series.SeriesCondition.LineMarkerTypePane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 上午9:57
 * 雷达图条件属性界面
 */
public class RadarPlotDataSeriesConditionPane extends DataSeriesConditionPane {
	private static final long serialVersionUID = 4134539843311969532L;

	protected void addStyleAction() {
        classPaneMap.put(AttrColor.class, new LabelColorPane(this));
        classPaneMap.put(AttrLineStyle.class, new LabelLineStylePane(this));
        classPaneMap.put(AttrMarkerType.class, new LineMarkerTypePane(this));
    }

    public Class<? extends Plot> class4Correspond() {
        return RadarPlot.class;
    }
}