package com.fr.design.chart.fun.impl;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.IndependentChartUIProvider;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.ChartsConfigPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StableUtils;


/**
 * Created by eason on 15/4/23.
 */
@Deprecated
public abstract class AbstractIndependentChartUI implements IndependentChartUIProvider {

    @Override
    public int currentAPILevel() {
        return -1;
    }

    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        return new AbstractChartAttrPane[0];
    }

    public ChartDataPane getChartDataPane(AttributeChangeListener listener){
        return new ChartDataPane(listener);
    }

    /**
     * 是否使用默认的界面，为了避免界面来回切换
     * @return 是否使用默认的界面
     */
    public boolean isUseDefaultPane(){
        return true;
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

    public ChartEditPane getChartEditPane(String plotID){ return StableUtils.construct(ChartEditPane.class);}

    public ChartsConfigPane getChartConfigPane(String plotID){return null;}


}