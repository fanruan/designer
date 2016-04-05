package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.Donut2DPlotDataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.style.series.Donut2DSeriesPane;
import com.fr.design.mainframe.chart.gui.style.series.Donut3DSeriesPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.DonutPlotPane;

/**
 * Created by eason on 15/4/21.
 */
public class DonutIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel {

    public AbstractChartTypePane getPlotTypePane(){
        return new DonutPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        return new CategoryPlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        return new CategoryPlotReportDataContentPane(parent);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return plot.isSupport3D() ? new Donut3DSeriesPane(parent, plot) : new Donut2DSeriesPane(parent, plot);
    }
    
    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return plot.isSupport3D() ? new DataSeriesConditionPane() : new Donut2DPlotDataSeriesConditionPane();
    }

    /**
     *图标路径
     * @return 图标路径
     */
    public String getIconPath(){
        return "com/fr/design/images/form/toolbar/ChartF-Donut.png";
    }

}