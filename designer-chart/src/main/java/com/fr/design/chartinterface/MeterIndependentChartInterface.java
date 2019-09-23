package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.MeterPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.MeterPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.style.series.MeterSeriesPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.MeterPlotPane;

/**
 * Created by eason on 15/4/21.
 */
public class MeterIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel {


    public AbstractChartTypePane getPlotTypePane() {
        return new MeterPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return new MeterPlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return new MeterPlotReportDataContentPane(parent);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        return new MeterSeriesPane(parent, plot);
    }

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/ChartF-Meter.png";
    }

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_Type_Meter");
    }

    @Override
    public String[] getSubName() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Type_Meter"),
                Toolkit.i18nText("Fine-Design_Chart_Type_Meter"),
                Toolkit.i18nText("Fine-Design_Chart_Type_Meter")
        };
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/meter.png",
                "com/fr/plugin/chart/demo/image/meterblue.png",
                "com/fr/plugin/chart/demo/image/metersimple.png"
        };
    }
}