package com.fr.design.chart.fun.impl;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.IndependentChartUIProvider;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.PiePlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.PiePlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.general.ComparatorUtils;

/**
 * Created by mengao on 2017/4/24.
 * 用户使用第三方图表需要继承的面板抽象类
 */
public abstract class AbstractIndependentChartUI4Custom implements IndependentChartUIProvider {

    int CURRENT_API_LEVEL = 3;

    @Override
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return null;
    }

    @Override
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return null;
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return getPlotSeriesPane();
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(){
        return null;
    }

    public boolean equals(Object obj) {
        return obj != null && ComparatorUtils.equals(obj.getClass(), this.getClass());
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new DataSeriesConditionPane();
    }

    @Override
    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
        return new ChartDataPane(listener);
    }

    @Override
    public boolean isUseDefaultPane(){
        return false;
    }


    @Override
    public String getIconPath() {
        return "com/fr/solution/plugin/chart/echarts/pie/images/pie.png";
    }

    @Override

    /**
     * plot面板的标题
     * 插件兼容
     */
    public String getPlotTypeTitle4PopupWindow(){
        return getPlotTypePane().title4PopupWindow();
    }
    @Override
    public int currentAPILevel() {
        return CURRENT_API_LEVEL;
    }
}






