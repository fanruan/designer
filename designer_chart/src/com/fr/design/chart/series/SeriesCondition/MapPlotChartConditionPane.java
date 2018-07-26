package com.fr.design.chart.series.SeriesCondition;

import com.fr.plugin.chart.type.ConditionKeyType;

/**
 * 地图的条件 参数下拉.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-11 下午03:44:40
 */
public class MapPlotChartConditionPane extends ChartConditionPane {

    @Override
    protected ConditionKeyType[] conditionKeyTypes() {
        return ConditionKeyType.OLD_MAP_CONDITION_KEY_TYPES;
    }
}