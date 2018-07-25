package com.fr.design.chart.series.SeriesCondition;

import com.fr.plugin.chart.type.ConditionKeyType;


/**
 * 饼图 条件显示 参数.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-11 下午03:48:32
 */
public class PiePlotChartConditionPane extends ChartConditionPane {

    @Override
    protected ConditionKeyType[] conditionKeyTypes() {
        return ConditionKeyType.NORMAL2_CONDITION_KEY_TYPES;
    }
}