package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.XYScatterPlotDataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.XYScatterPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.XYScatterPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.style.series.XYScatterSeriesPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.XYScatterPlotPane;

/**
 * Created by eason on 15/4/21.
 */
public class XYScatterIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel {


    public AbstractChartTypePane getPlotTypePane() {
        return new XYScatterPlotPane();
    }


    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return new XYScatterPlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return new XYScatterPlotReportDataContentPane(parent);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        return new XYScatterSeriesPane(parent, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot) {
        return plot.isSupportTrendLine() ? new XYScatterPlotDataSeriesConditionPane() : new DataSeriesConditionPane();
    }

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/ChartF-XYScatter.png";
    }

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_Type_XYScatter");
    }

    @Override
    public String[] getSubName() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Type_XYScatter")
        };
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/point.png"
        };
    }
}