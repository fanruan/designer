package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.design.condition.ConditionAttributesPane;
import com.fr.plugin.chart.designer.component.VanChartLineTypePane;
import com.fr.plugin.chart.designer.component.VanChartLineWidthPane;

/**
 * Created by Mitisky on 15/12/30.
 */
public class VanChartLineWidthConditionPane extends VanChartLineTypeConditionPane {
    private static final long serialVersionUID = 8053501518246410603L;

    public VanChartLineWidthConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    protected VanChartLineTypePane createLinePane() {
        return new VanChartLineWidthPane();
    }
}