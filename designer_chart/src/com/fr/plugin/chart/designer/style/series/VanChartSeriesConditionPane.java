package com.fr.plugin.chart.designer.style.series;

import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.plugin.chart.type.ConditionKeyType;

/**
 * 只对系列进行设置
 */
public class VanChartSeriesConditionPane extends ChartConditionPane {

    @Override
    protected ConditionKeyType[] conditionKeyTypes() {
        return ConditionKeyType.SERIES_CONDITION_KEY_TYPES;
    }
}