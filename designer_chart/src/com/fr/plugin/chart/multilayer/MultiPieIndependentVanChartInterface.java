package com.fr.plugin.chart.multilayer;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.plugin.chart.multilayer.data.MultiPiePlotReportDataContentPane;
import com.fr.plugin.chart.multilayer.data.MultiPiePlotTableDataContentPane;
import com.fr.plugin.chart.multilayer.other.VanChartMultiPieConditionPane;
import com.fr.plugin.chart.multilayer.style.VanChartMultiPieSeriesPane;
import com.fr.plugin.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by Fangjie on 2016/6/15.
 */
public class MultiPieIndependentVanChartInterface extends AbstractIndependentVanChartUI {
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartMultiPiePlotPane();
    }

    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/multiPie.png";
    }

    @Override
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        return new MultiPiePlotTableDataContentPane(parent);
    }

    @Override
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        return new MultiPiePlotReportDataContentPane(parent);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartMultiPieSeriesPane(parent, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartMultiPieConditionPane(plot);
    }

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartMultiPiePlotPane.TITLE;
    }
}
