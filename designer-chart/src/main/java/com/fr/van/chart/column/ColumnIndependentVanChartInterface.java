package com.fr.van.chart.column;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.vanchart.AbstractMultiCategoryVanChartUI;

/**
 * Created by Mitisky on 15/9/24.
 */
public class ColumnIndependentVanChartInterface extends AbstractMultiCategoryVanChartUI {

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_New_Column");
    }

    @Override
    public String[] getSubName() {
        String column = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_New_Column");
        String stack = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Stacked");
        String percent = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Percent");
        return new String[]{
                column,
                stack + column,
                percent + stack + column,
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Mode_Custom")
        };
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/3.png",
                "com/fr/plugin/chart/demo/image/4.png",
                "com/fr/plugin/chart/demo/image/5.png",
                "com/fr/plugin/chart/demo/image/6.png",
        };
    }

    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/column.png";
    }

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartColumnPlotPane();
    }


    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartColumnConditionPane(plot);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartColumnSeriesPane(parent, plot);
    }

//    @Override
//    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
//        return new AbstractDataPane(listener) {
//            @Override
//            protected SingleDataPane createSingleDataPane() {
//                return new SingleDataPane(new MultiCategoryDataSetFieldsPane(), new MultiCategoryCellDataFieldsPane());
//            }
//        };
//    }
}