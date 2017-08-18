package com.fr.plugin.chart.designer.style.axis.bar;

import com.fr.plugin.chart.designer.style.axis.VanChartAxisScrollPaneWithTypeSelect;

/**
 * Created by Mitisky on 16/6/8.
 * y轴是可以选择坐标轴类型的
 */
public class VanChartYAxisScrollPaneWithTypeSelect extends VanChartAxisScrollPaneWithTypeSelect{
    @Override
    protected boolean isXAxis() {
        return false;
    }
}
