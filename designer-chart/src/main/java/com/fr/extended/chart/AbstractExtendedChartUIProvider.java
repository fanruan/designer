package com.fr.extended.chart;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.ChartTypeUIProvider;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.ChartsConfigPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.stable.fun.impl.AbstractProvider;

/**
 * Created by shine on 2018/3/2.
 */
public abstract class AbstractExtendedChartUIProvider extends AbstractProvider implements ChartTypeUIProvider {

    protected abstract AbstractExtendedChartTableDataPane getTableDataSourcePane();

    protected abstract AbstractReportDataContentPane getReportDataSourcePane();

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String[] getSubName() {
        return new String[0];
    }

    @Override
    public boolean needChartChangePane() {
        return false;
    }

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new ExtendedTypePane();
    }

    @Override
    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
        return new ChartDataPane(listener);
    }

    @Override
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return getTableDataSourcePane();
    }

    @Override
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return getReportDataSourcePane();
    }

    @Override
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener) {
        return new AbstractChartAttrPane[0];
    }

    @Override
    public boolean isUseDefaultPane() {
        return false;
    }

    @Override
    public int currentAPILevel() {
        return CURRENT_API_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    @Override
    public ChartEditPane getChartEditPane(String plotID) {
        return new ChartEditPane();
    }

    @Override
    public ConditionAttributesPane getPlotConditionPane(Plot plot) {
        return null;
    }

    @Override
    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        return null;
    }

    @Override
    public ChartsConfigPane getChartConfigPane(String plotID) {
        return null;
    }
}
