package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.chart.series.SeriesCondition.impl.Bar2DTrendLineDSConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.Bar3DPlotDataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.BarPlotDataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.style.series.Bar2DSeriesPane;
import com.fr.design.mainframe.chart.gui.style.series.Bar3DSeriesPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.BarPlotPane;

/**
 * Created by eason on 15/4/21.
 */
public class BarIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel {

    public AbstractChartTypePane getPlotTypePane(){
        return new BarPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        return new CategoryPlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        return new CategoryPlotReportDataContentPane(parent);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return plot.isSupport3D() ? new Bar3DSeriesPane(parent, plot) : new Bar2DSeriesPane(parent, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return plot.isSupport3D() ? new Bar3DPlotDataSeriesConditionPane()
                                    : (plot.isSupportTrendLine() ? new Bar2DTrendLineDSConditionPane() : new BarPlotDataSeriesConditionPane());
    }

    /**
     *图标路径
     * @return 图标路径
     */
    public String getIconPath(){
        return "com/fr/design/images/form/toolbar/ChartF-Bar.png";
    }

}