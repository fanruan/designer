package com.fr.van.chart.map.designer.other.condition.item;

import com.fr.design.condition.ConditionAttributesPane;

import com.fr.van.chart.designer.other.condition.item.VanChartBubbleSetConditionPane;

/**
 * Created by Mitisky on 16/5/23.
 */
public class VanChartBubbleMarkerConditionPane extends VanChartBubbleSetConditionPane {
    public VanChartBubbleMarkerConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    protected String getItemLabelString() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Marker");
    }

    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Marker");
    }


}
