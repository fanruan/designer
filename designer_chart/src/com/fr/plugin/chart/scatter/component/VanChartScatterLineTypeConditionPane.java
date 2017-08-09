package com.fr.plugin.chart.scatter.component;

import com.fr.design.condition.ConditionAttributesPane;
import com.fr.plugin.chart.designer.component.VanChartLineTypePane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartLineTypeConditionPane;

/**
 * 折线图，线型相关条件属性
 */
public class VanChartScatterLineTypeConditionPane extends VanChartLineTypeConditionPane {
    private static final long serialVersionUID = 1924676751313839477L;

    public VanChartScatterLineTypeConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    @Override
    protected VanChartLineTypePane createLinePane() {
        return new VanChartScatterLineTypePane();
    }
}