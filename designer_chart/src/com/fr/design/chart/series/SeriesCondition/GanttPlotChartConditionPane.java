package com.fr.design.chart.series.SeriesCondition;

import com.fr.plugin.chart.type.ConditionKeyType;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 下午3:13
 */
public class GanttPlotChartConditionPane extends ChartConditionPane {

    @Override
    protected ConditionKeyType[] conditionKeyTypes() {
        return ConditionKeyType.OLD_GANTT_CONDITION_KEY_TYPES;
    }
}