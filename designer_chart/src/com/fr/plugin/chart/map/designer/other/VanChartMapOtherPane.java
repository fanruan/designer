package com.fr.plugin.chart.map.designer.other;

import com.fr.chart.chartattr.Chart;
import com.fr.design.beans.BasicBeanPane;
import com.fr.plugin.chart.designer.other.VanChartOtherPane;

/**
 * Created by Mitisky on 16/5/20.
 */
public class VanChartMapOtherPane extends VanChartOtherPane {
    @Override
    protected BasicBeanPane<Chart> createInteractivePane() {
        return new VanChartMapInteractivePane();
    }

    @Override
    protected BasicBeanPane<Chart> createConditionAttrPane() {
        return new VanChartMapConditionAttrPane();
    }

}
