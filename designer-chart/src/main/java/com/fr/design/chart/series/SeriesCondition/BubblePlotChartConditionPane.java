/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.ChartConstants;

/**
 * 气泡图 条件显示  参数 控制界面.
 * Created by kunsnat on 14-3-11.
 * kunsnat@gmail.com
 */
public class BubblePlotChartConditionPane extends ChartConditionPane{

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