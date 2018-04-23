package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.chartattr.*;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesCustomConditionPane;
import com.fr.design.condition.ConditionAttributesPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 上午10:25
 * 系列的条件属性 工厂. 
 * 不包括组合图.(ps 组合图是用组合类型界面拼凑的) 
 */
public class DataSeriesConditionPaneFactory {
    private static final String TREND = ".TrendLine";

    private static Map<String, Class<? extends ConditionAttributesPane>> map = new HashMap<String, Class<? extends ConditionAttributesPane>>();

    static {
        map.put(Area3DPlot.class.getName(), Area3DPlotDataSeriesConditionPane.class);
        map.put(AreaPlot.class.getName(), AreaPlotDataSeriesCondtionPane.class);
        map.put(BarPlot.class.getName(), BarPlotDataSeriesConditionPane.class);
        map.put(Bar3DPlot.class.getName(), Bar3DPlotDataSeriesConditionPane.class);
        map.put(Bar2DPlot.class.getName() + TREND, Bar2DTrendLineDSConditionPane.class);
        map.put(BubblePlot.class.getName(), BubblePlotDataSeriesConditionPane.class);
        map.put(GanttPlot.class.getName(), GanttPlotDataSeriesConditionPane.class);
        map.put(LinePlot.class.getName() + TREND, LinePlotDataSeriesConditionPane.class);
        map.put(PiePlot.class.getName(), PiePlotDataSeriesConditionPane.class);
        map.put(Pie3DPlot.class.getName(), Pie3DPlotDataSeriesConditionPane.class);
        map.put(RadarPlot.class.getName(), RadarPlotDataSeriesConditionPane.class);
        map.put(StockPlot.class.getName(), StockPlotDataSeriesConditionPane.class);
        map.put(XYScatterPlot.class.getName() + TREND, XYScatterPlotDataSeriesConditionPane.class);
        map.put(MapPlot.class.getName(), MapPlotDataSeriesConditionPane.class);
        map.put(CustomPlot.class.getName(), DataSeriesCustomConditionPane.class);
        map.put(Donut2DPlot.class.getName(), Donut2DPlotDataSeriesConditionPane.class);
    }

    private DataSeriesConditionPaneFactory() {

    }

    /**
     * 找到Plot条件界面对应的DataSeries的.
     * @param plot  对应Plot
     * @return 返回class对应的属性界面.
     */
    public static Class<? extends ConditionAttributesPane> findConfitionPane4DataSeries(Plot plot) {
        if (plot == null) {
            return DataSeriesConditionPane.class;
        }
        boolean isSupportTrendLine = plot.isSupportTrendLine();
        return searchSuitableClass(plot.getClass(), isSupportTrendLine);
    }

    private static Class<? extends ConditionAttributesPane> searchSuitableClass(Class plotCls, boolean isSupportTrendLine) {
        if (plotCls == null) {
            return DataSeriesConditionPane.class;
        }
        String plotClsName = plotCls.getName();
        if (isSupportTrendLine) {
            plotClsName += TREND;
        }
        Class<? extends ConditionAttributesPane> resClass = map.get(plotClsName);
        if (resClass != null) {
            return resClass;
        }
        return searchSuitableClass(plotCls.getSuperclass(), isSupportTrendLine);
    }
}