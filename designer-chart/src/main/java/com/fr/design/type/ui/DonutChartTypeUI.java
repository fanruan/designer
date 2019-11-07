package com.fr.design.type.ui;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.Donut2DPlotDataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.i18n.Toolkit;
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
public class DonutChartTypeUI extends AbstractIndependentChartUIWithAPILevel {


    public AbstractChartTypePane getPlotTypePane() {
        return new DonutPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return new CategoryPlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return new CategoryPlotReportDataContentPane(parent);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        return plot.isSupport3D() ? new Donut3DSeriesPane(parent, plot) : new Donut2DSeriesPane(parent, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot) {
        return plot.isSupport3D() ? new DataSeriesConditionPane() : new Donut2DPlotDataSeriesConditionPane();
    }

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/ChartF-Donut.png";
    }

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_Type_Donut");
    }

    @Override
    public String[] getSubName() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Type_Donut"),
                Toolkit.i18nText("Fine-Design_Chart_3D") + Toolkit.i18nText("Fine-Design_Chart_Type_Donut")
        };
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/donut2D.png",
                "com/fr/plugin/chart/demo/image/donut3D.png"
        };
    }
}