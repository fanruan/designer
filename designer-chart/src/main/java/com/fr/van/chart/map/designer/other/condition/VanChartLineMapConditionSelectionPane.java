package com.fr.van.chart.map.designer.other.condition;

import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.plugin.chart.type.ConditionKeyType;

/**
 * Created by hufan on 2016/12/26.
 */
public class VanChartLineMapConditionSelectionPane extends ChartConditionPane {

    @Override
    protected ConditionKeyType[] conditionKeyTypes() {
        return ConditionKeyType.LINE_MAP_CONDITION_KEY_TYPES;
    }
}
