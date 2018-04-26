package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.AttrMarkerType;
import com.fr.chart.base.AttrTrendLine;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.design.chart.series.SeriesCondition.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 上午9:53
 * 散点图的条件属性界面
 */
public class XYScatterPlotDataSeriesConditionPane extends DataSeriesConditionPane {
	private static final long serialVersionUID = -2552214303519434824L;

	protected void addStyleAction() {
        classPaneMap.put(AttrColor.class, new LabelColorPane(this));
        classPaneMap.put(AttrLineStyle.class, new LabelLineStylePane(this));
        classPaneMap.put(AttrMarkerType.class, new LineMarkerTypePane(this));
    }

    protected void addTrendLineAction() {
        classPaneMap.put(AttrTrendLine.class, new TrendLinePane(this));
    }
    
    public Class<? extends Plot> class4Correspond() {
        return XYScatterPlot.class;
    }
}