/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.chart.series.SeriesCondition;

import com.fr.plugin.chart.type.ConditionKeyType;

/**
 * 气泡图 条件显示  参数 控制界面.
 * Created by kunsnat on 14-3-11.
 * kunsnat@gmail.com
 */
public class BubblePlotChartConditionPane extends ChartConditionPane{

    @Override
    protected ConditionKeyType[] conditionKeyTypes() {
        return ConditionKeyType.NORMAL2_CONDITION_KEY_TYPES;
    }
}