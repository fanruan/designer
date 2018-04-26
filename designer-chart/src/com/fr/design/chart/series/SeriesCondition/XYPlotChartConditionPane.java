/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.ChartConstants;

/**
 * 散点图 条件属性 显示参数 界面.
 * Created by kunsnat on 14-3-10.
 * kunsnat@gmail.com
 */
public class XYPlotChartConditionPane extends ChartConditionPane {

    /**
     * 界面条件序号 列表
     * @return 返回条件列表.
     */
    public String[] columns2Populate() {
        return new String[]{
                ChartConstants.SERIES_INDEX,
                ChartConstants.SERIES_NAME,
                ChartConstants.VALUE
        };
    }
}