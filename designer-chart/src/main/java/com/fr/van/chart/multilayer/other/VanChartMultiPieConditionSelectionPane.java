package com.fr.van.chart.multilayer.other;

import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.plugin.chart.type.ConditionKeyType;

/**
 * Created by Fangjie on 2016/6/16.
 */
public class VanChartMultiPieConditionSelectionPane extends ChartConditionPane {

    @Override
    protected ConditionKeyType[] conditionKeyTypes() {
        return ConditionKeyType.MULTI_PIE_CONDITION_KEY_TYPES;
    }
}
