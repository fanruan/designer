package com.fr.van.chart.custom.other;

import com.fr.chart.chartattr.Chart;
import com.fr.design.beans.BasicBeanPane;
import com.fr.van.chart.designer.other.VanChartOtherPane;

/**
 * Created by Fangjie on 2016/4/28.
 */
public class VanChartCustomOtherPane extends VanChartOtherPane {
    protected BasicBeanPane<Chart> createInteractivePane() {
        return new VanChartCustomInteractivePane();
    }

    @Override
    protected BasicBeanPane<Chart> createConditionAttrPane() {
        return new VanChartCustomConditionAttrPane();
    }
}
