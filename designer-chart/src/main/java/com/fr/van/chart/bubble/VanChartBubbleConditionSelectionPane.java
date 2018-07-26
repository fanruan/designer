package com.fr.van.chart.bubble;

import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.plugin.chart.type.ConditionKeyType;

/**
 * Created by Mitisky on 16/3/31.
 */
public class VanChartBubbleConditionSelectionPane extends ChartConditionPane {

    @Override
    protected ConditionKeyType[] conditionKeyTypes() {
        return ConditionKeyType.BUBBLE_CONDITION_KEY_TYPES;
    }
}
