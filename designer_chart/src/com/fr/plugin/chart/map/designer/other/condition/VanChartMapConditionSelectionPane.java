package com.fr.plugin.chart.map.designer.other.condition;

import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.plugin.chart.type.ConditionKeyType;

/**
 * Created by Mitisky on 16/6/1.
 */
public class VanChartMapConditionSelectionPane extends ChartConditionPane {

    @Override
    protected ConditionKeyType[] conditionKeyTypes() {
        return ConditionKeyType.MAP_CONDITION_KEY_TYPES;
    }
}
