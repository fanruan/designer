package com.fr.van.chart.designer.style.axis.bar;

import com.fr.van.chart.designer.style.axis.VanChartAxisScrollPaneWithOutTypeSelect;

/**
 * Created by Mitisky on 16/6/8.
 * 无类型选择. x轴数值轴
 */
public class VanChartXAxisScrollPaneWithOutTypeSelect extends VanChartAxisScrollPaneWithOutTypeSelect {
    @Override
    protected boolean isXAxis() {
        return true;
    }
}
