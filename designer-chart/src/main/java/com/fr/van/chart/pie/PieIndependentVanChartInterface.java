package com.fr.van.chart.pie;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.custom.component.CategoryCustomPlotTableDataContentPane;
import com.fr.van.chart.custom.component.VanChartDataPane;
import com.fr.van.chart.vanchart.AbstractIndependentVanChartUI;


/**
 * Created by Mitisky on 15/8/4.
 */
public class PieIndependentVanChartInterface extends AbstractIndependentVanChartUI {

    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/pie.png";
    }

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_New_Pie");
    }

    @Override
    public String[] getSubName() {
        return new String[]{
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_New_Pie"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_SameArc_Pie"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_DifferArc_Pie")
        };
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/0.png",
                "com/fr/plugin/chart/demo/image/1.png",
                "com/fr/plugin/chart/demo/image/2.png"
        };
    }


    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartPiePlotPane();
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartPieConditionPane(plot);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartPieSeriesPane(parent, plot);
    }


    /**
     * 如果parent為VanChartDataPane
     * 則使用custom使用的數據配置面板
     * @param plot
     * @param parent
     * @return
     */
    @Override
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        return (parent instanceof VanChartDataPane) ? new CategoryCustomPlotTableDataContentPane(parent) : new CategoryPlotTableDataContentPane(parent);
    }
}