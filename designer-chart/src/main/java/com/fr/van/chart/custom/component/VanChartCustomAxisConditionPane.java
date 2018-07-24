package com.fr.van.chart.custom.component;

import com.fr.design.gui.ilable.UILabel;

import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.van.chart.column.VanChartCustomStackAndAxisConditionPane;

import java.awt.Component;
/**
 * 自定义坐标轴设置
 * 散点图和气泡图用到
 * 堆积和百分比属性为false
 */
public class VanChartCustomAxisConditionPane extends VanChartCustomStackAndAxisConditionPane {

    public VanChartCustomAxisConditionPane(){

    }

    protected Component[][] getDeployComponents() {
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("ChartF-X_Axis")),XAxis},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("ChartF-Y_Axis")),YAxis},
        };

        return components;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Custom_Axis");
    }


    @Override
    protected void updateStackAndPercent(AttrSeriesStackAndAxis seriesStackAndAxis) {
        seriesStackAndAxis.setStacked(false);
        seriesStackAndAxis.setPercentStacked(false);
    }

}