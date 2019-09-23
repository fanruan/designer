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
import com.fr.locale.InterProviderFactory;

/**
 * Created by eason on 15/4/21.
 */
public class BarIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel {

    public AbstractChartTypePane getPlotTypePane() {
        return new BarPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return new CategoryPlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return new CategoryPlotReportDataContentPane(parent);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        return plot.isSupport3D() ? new Bar3DSeriesPane(parent, plot) : new Bar2DSeriesPane(parent, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot) {
        return plot.isSupport3D() ? new Bar3DPlotDataSeriesConditionPane()
                : (plot.isSupportTrendLine() ? new Bar2DTrendLineDSConditionPane() : new BarPlotDataSeriesConditionPane());
    }

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/ChartF-Bar.png";
    }

    @Override
    public String getName() {
        return InterProviderFactory.getProvider().getLocText("Fine-Engine_Chart_Type_Bar");
    }

    @Override
    public String[] getSubName() {
        String chartName = InterProviderFactory.getProvider().getLocText("Fine-Engine_Chart_Type_Bar");
        String stackChartName = InterProviderFactory.getProvider().getLocText("Fine-Engine_Chart_Type_Stacked") + chartName;
        String perStackChartName = InterProviderFactory.getProvider().getLocText("Fine-Engine_Chart_Use_Percent") + stackChartName;
        String chartName3D = InterProviderFactory.getProvider().getLocText("Fine-Engine_Chart_3D") + chartName;
        String chartNameHoriz3D = chartName3D + "(" + InterProviderFactory.getProvider().getLocText("Fine-Engine_Report_Utils_Left_To_Right") + ")";
        String stackChartName3D = InterProviderFactory.getProvider().getLocText("Fine-Engine_Chart_3D") + stackChartName;
        String perStackChartName3D = InterProviderFactory.getProvider().getLocText("Fine-Engine_Chart_3D") + perStackChartName;

        //依次，条形图-旧版本、堆积条形图-旧版本、百分比堆积条形图-旧版本、三维条形图-旧版本、三维条形图-旧版本(横向)、三维堆积条形图-旧版本、三维百分比堆积条形图-旧版本
        return new String[]{
                chartName,
                stackChartName,
                perStackChartName,
                chartName3D,
                chartNameHoriz3D,
                stackChartName3D,
                perStackChartName3D
        };
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/bar.png",
                "com/fr/plugin/chart/demo/image/barstacked.png",
                "com/fr/plugin/chart/demo/image/barstackedpercent.png",
                "com/fr/plugin/chart/demo/image/bar3D.png",
                "com/fr/plugin/chart/demo/image/bar3D(cross).png",
                "com/fr/plugin/chart/demo/image/barstacked3D.png",
                "com/fr/plugin/chart/demo/image/barstackedpercent3D.png"
        };
    }

}