package com.fr.van.chart.designer.other.condition.item;

import com.fr.design.condition.ConditionAttributesPane;
import com.fr.van.chart.designer.component.VanChartLineTypePane;
import com.fr.van.chart.designer.component.VanChartLineTypePaneWithoutDashed;

public class VanChartLineTypeConditionPaneWithoutDashed extends VanChartLineTypeConditionPane {

    public VanChartLineTypeConditionPaneWithoutDashed(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    protected VanChartLineTypePane createLinePane() {
        return new VanChartLineTypePaneWithoutDashed();
    }
}
