package com.fr.van.chart.pie;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
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

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartPiePlotPane.TITLE;
    }
}