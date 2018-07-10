package com.fr.van.chart.drillmap.designer.other;

import com.fr.chart.chartattr.Chart;
import com.fr.design.beans.BasicBeanPane;
import com.fr.van.chart.map.designer.other.VanChartMapOtherPane;

/**
 * Created by Mitisky on 16/6/29.
 */
public class VanChartDrillMapOtherPane extends VanChartMapOtherPane {
    @Override
    protected BasicBeanPane<Chart> createInteractivePane() {
        return new VanChartDrillMapInteractivePane();
    }

}
