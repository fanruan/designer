package com.fr.van.chart.area;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.vanchart.AbstractMultiCategoryVanChartUI;

/**
 * Created by Mitisky on 15/11/18.
 */
public class AreaIndependentVanChartInterface extends AbstractMultiCategoryVanChartUI {
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/area.png";
    }

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_New_Area");
    }

    @Override
    public String[] getSubName() {
        String area = Toolkit.i18nText("Fine-Design_Chart_New_Area");
        String stack = Toolkit.i18nText("Fine-Design_Chart_Stacked");
        String percent = Toolkit.i18nText("Fine-Design_Chart_Use_Percent");
        return new String[]{
                area,
                stack + area,
                percent + stack + area,
                Toolkit.i18nText("Fine-Design_Chart_Mode_Custom")
        };
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/14.png",
                "com/fr/plugin/chart/demo/image/15.png",
                "com/fr/plugin/chart/demo/image/16.png",
                "com/fr/plugin/chart/demo/image/17.png"
        };
    }

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartAreaPlotPane();
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartAreaConditionPane(plot);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartAreaSeriesPane(parent, plot);
    }

}