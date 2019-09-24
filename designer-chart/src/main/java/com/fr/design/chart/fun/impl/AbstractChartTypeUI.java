package com.fr.design.chart.fun.impl;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.ChartTypeUIProvider;
import com.fr.design.chartx.impl.AbstractDataPane;
import com.fr.design.chartx.impl.AbstractOtherPane;
import com.fr.design.chartx.impl.DefaultTypePane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.ChartsConfigPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by shine on 2019/09/03.
 */
@API(level = ChartTypeUIProvider.CURRENT_API_LEVEL)
public abstract class AbstractChartTypeUI extends AbstractProvider implements ChartTypeUIProvider {

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new DefaultTypePane();
    }

    @Override
    public abstract AbstractDataPane getChartDataPane(AttributeChangeListener listener);

    @Override
    public abstract AbstractOtherPane[] getAttrPaneArray(AttributeChangeListener listener);

    @Override
    public String[] getSubName() {
        return new String[]{getName()};
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
    public boolean needChartChangePane() {
        return false;
    }

    @Override
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return null;
    }

    @Override
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return null;
    }

    @Override
    public boolean isUseDefaultPane() {
        return false;
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