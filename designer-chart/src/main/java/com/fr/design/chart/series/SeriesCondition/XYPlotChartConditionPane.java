/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.chart.series.SeriesCondition;

import com.fr.plugin.chart.type.ConditionKeyType;

/**
 * 散点图 条件属性 显示参数 界面.
 * Created by kunsnat on 14-3-10.
 * kunsnat@gmail.com
 */
public class XYPlotChartConditionPane extends ChartConditionPane {

    @Override
    protected ConditionKeyType[] conditionKeyTypes() {
        return ConditionKeyType.NORMAL2_CONDITION_KEY_TYPES;
    }
}