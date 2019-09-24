package com.fr.van.chart.scatter.component;

import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.type.LineStyle;
import com.fr.plugin.chart.type.LineType;
import com.fr.van.chart.designer.component.VanChartLineTypePane;

import java.awt.Component;

/**
 * line相关设置
 */
public class VanChartScatterLineTypePane extends VanChartLineTypePane {

    @Override
    protected void createLineStyle() {
        String[] textArray = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Normal_Line"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_CurveLine")};
        lineStyle = new UIButtonGroup<LineStyle>(textArray, new LineStyle[]{LineStyle.NORMAL, LineStyle.CURVE});
    }

    @Override
    protected Component[][] createContentComponent(Component[] lineStyleComponent, Component[] nullValueBreakComponent) {
        return new Component[][]{
                lineStyleComponent
        };
    }

    @Override
    protected VanChartAttrLine initVanChartAttrLine() {
        VanChartAttrLine attrLine = new VanChartAttrLine();
        //默认为无线型，且默認空值不斷開
        attrLine.setLineType(LineType.NONE);
        attrLine.setNullValueBreak(false);
        return attrLine;
    }

}