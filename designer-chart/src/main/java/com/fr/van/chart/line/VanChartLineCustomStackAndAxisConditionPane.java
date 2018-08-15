package com.fr.van.chart.line;

import com.fr.design.gui.ilable.UILabel;

import com.fr.van.chart.column.VanChartCustomStackAndAxisConditionPane;

import java.awt.Component;

/**
 * 堆积和坐标轴（没有百分比）
 */
public class VanChartLineCustomStackAndAxisConditionPane extends VanChartCustomStackAndAxisConditionPane {
    protected Component[][] getDeployComponents() {
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_X_Axis")),XAxis},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Y_Axis")),YAxis},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Stacked")),isStacked},
        };

        return components;
    }
}