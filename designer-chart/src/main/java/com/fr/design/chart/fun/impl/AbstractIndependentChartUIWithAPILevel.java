package com.fr.design.chart.fun.impl;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.ChartTypeUIProvider;
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
 * Created by Mitisky on 16/3/7.
 */
public abstract class AbstractIndependentChartUIWithAPILevel implements ChartTypeUIProvider {
    //这个不能改,是做兼容用的
    //2016.10.14-11.24号的8.0jar因为改了这个为3,不会提示5.26号之前的插件更新
    private static final int OLD_PLUGIN_LEVEL = -2;

    @Override
    //以前的插件没有覆写这个方法,所以始终获取到-2,比当前level低,提示更新.
    //新的插件编译进去的是当前LEVEL,当之后LEVEL增加,会比编译进去的LEVEL大,提示更新.
    public int currentAPILevel() {
        return OLD_PLUGIN_LEVEL;
    }

    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        return new AbstractChartAttrPane[0];
    }

    public ChartDataPane getChartDataPane(AttributeChangeListener listener){
        return new ChartDataPane(listener);
    }

    @Override
    public String[] getSubName() {
        return new String[0];
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

    @Override
    public boolean needChartChangePane() {
        return true;
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new DataSeriesConditionPane();
    }

    public ChartEditPane getChartEditPane(String plotID){ return StableUtils.construct(ChartEditPane.class);}

    public ChartsConfigPane getChartConfigPane(String plotID){return null;}
}
