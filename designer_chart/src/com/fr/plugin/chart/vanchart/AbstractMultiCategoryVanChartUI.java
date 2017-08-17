package com.fr.plugin.chart.vanchart;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.designer.data.VanChartMoreCateReportDataContentPane;
import com.fr.plugin.chart.designer.data.VanChartMoreCateTableDataContentPane;

/**
 * Created by mengao on 2017/7/6.
 */
public abstract class AbstractMultiCategoryVanChartUI extends  AbstractIndependentVanChartUI {
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        //自定义组合图特殊处理
        if (((VanChartPlot)plot).isInCustom() && ((VanChartPlot)plot).getCustomType().equals("CUSTOM")) {
            return new CategoryPlotTableDataContentPane(parent);
        }
        return new VanChartMoreCateTableDataContentPane(parent);    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        //自定义组合图特殊处理
        if (((VanChartPlot)plot).isInCustom() && ((VanChartPlot)plot).getCustomType().equals("CUSTOM")) {
            return new CategoryPlotReportDataContentPane(parent);
        }
        return new VanChartMoreCateReportDataContentPane(parent);
    }
}
