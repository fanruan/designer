/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrColor;
import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelColorPane;

/**
 * 气泡图 条件显示 界面.
 * Created by kunsnat on 14-3-11.
 * kunsnat@gmail.com
 */
public class BubblePlotDataSeriesConditionPane extends DataSeriesConditionPane {

    protected void addStyleAction() {
        classPaneMap.put(AttrColor.class, new LabelColorPane(this));
    }

    /**
     * 返回对应的class
     * @return 返回class
     */
    public Class<? extends Plot> class4Correspond() {
        return BubblePlot.class;
    }
}