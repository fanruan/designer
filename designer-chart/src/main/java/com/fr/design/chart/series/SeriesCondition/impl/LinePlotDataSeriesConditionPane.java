package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.AttrMarkerType;
import com.fr.chart.base.AttrTrendLine;
import com.fr.chart.chartattr.LinePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.*;

/**
 * 折线图(无趋势线)中的条件属性界面.
 */
public class LinePlotDataSeriesConditionPane extends DataSeriesConditionPane {
	private static final long serialVersionUID = 90767073697041627L;

	protected void addStyleAction() {
        classPaneMap.put(AttrColor.class, new LabelColorPane(this));
        classPaneMap.put(AttrLineStyle.class, new LabelLineStylePane(this));
        classPaneMap.put(AttrMarkerType.class, new LineMarkerTypePane(this));
    }
	
	protected void addTrendLineAction() {
        classPaneMap.put(AttrTrendLine.class, new TrendLinePane(this));
    }
	
    public Class<? extends Plot> class4Correspond() {
        return LinePlot.class;
    }
}