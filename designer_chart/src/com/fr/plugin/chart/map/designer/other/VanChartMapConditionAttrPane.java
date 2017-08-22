package com.fr.plugin.chart.map.designer.other;

import com.fr.plugin.chart.designer.other.VanChartConditionAttrContentPane;
import com.fr.plugin.chart.designer.other.VanChartConditionAttrPane;

/**
 * Created by Mitisky on 16/5/20.
 */
public class VanChartMapConditionAttrPane extends VanChartConditionAttrPane{
    protected VanChartConditionAttrContentPane createConditionAttrContentPane() {
        return new VanChartMapConditionAttrContentPane();
    }
}
