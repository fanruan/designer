package com.fr.design.mainframe;

import com.fr.chart.base.ChartTypeValueCollection;
import com.fr.design.mainframe.chart.gui.type.*;
import com.fr.general.FRLogger;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 * 图表设计器工具栏面板的工厂类
 */
public class PlotToolBarFactory {
    private static HashMap<ChartTypeValueCollection,Class<? extends PlotPane4ToolBar>> panes4NormalPlot =
            new HashMap<ChartTypeValueCollection, Class<? extends PlotPane4ToolBar>>();

    private static HashMap<ChartTypeValueCollection,Class<? extends AbstractMapPlotPane4ToolBar>> panes4MapPlot =
            new HashMap<ChartTypeValueCollection, Class<? extends AbstractMapPlotPane4ToolBar>>();

    static {
        panes4NormalPlot.put(ChartTypeValueCollection.COLUMN, ColumnPlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.LINE, LinePlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.BAR, BarPlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.PIE, PiePlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.AREA,AreaPlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.XYSCATTER,XYSCatterPlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.BUBBLE,BubblePlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.RADAR,RadarPlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.STOCK,StockPlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.METER,MeterPlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.RANGE,RangePlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.COMB,CustomPlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.GANTT,GanttPlotPane4ToolBar.class);
        panes4NormalPlot.put(ChartTypeValueCollection.DONUT,DonutPlotPane4ToolBar.class);

        panes4MapPlot.put(ChartTypeValueCollection.MAP,MapPlotPane4ToolBar.class);
        panes4MapPlot.put(ChartTypeValueCollection.GIS,GisMapPlotPane4ToolBar.class);
    }

    private PlotToolBarFactory(){

    }

    /**
     * 为了地图和gis以外的图表类型创建工具栏
     * @param type 图表类型
     * @param chartDesigner 图表设计器
     * @return 工具栏
     */
    public static PlotPane4ToolBar createToolBar4NormalPlot(ChartTypeValueCollection type,ChartDesigner chartDesigner){
        if(!panes4NormalPlot.containsKey(type)){
           return new ColumnPlotPane4ToolBar(chartDesigner);
        }
        try {
            Class<? extends PlotPane4ToolBar> cls = panes4NormalPlot.get(type);
            Constructor<? extends PlotPane4ToolBar > constructor = cls.getConstructor(ChartDesigner.class);
            return constructor.newInstance(chartDesigner);
        }catch (Exception e){
            FRLogger.getLogger().error(e.getMessage());
            return new ColumnPlotPane4ToolBar(chartDesigner);
        }
    }

    /**
     *为地图和gis创建工具栏
     * @param type 类型
     * @param chartDesigner 图表设计器
     * @return 工具栏
     */
    public static AbstractMapPlotPane4ToolBar createToolBar4MapPlot(ChartTypeValueCollection type,ChartDesigner chartDesigner){
        if(!panes4MapPlot.containsKey(type)){
           return new MapPlotPane4ToolBar(chartDesigner);
        }
        try {
            Class<? extends AbstractMapPlotPane4ToolBar> cls = panes4MapPlot.get(type);
            Constructor<? extends AbstractMapPlotPane4ToolBar > constructor = cls.getConstructor(ChartDesigner.class);
            return constructor.newInstance(chartDesigner);
        }catch (Exception e){
            FRLogger.getLogger().error(e.getMessage());
            return new MapPlotPane4ToolBar(chartDesigner);
        }
    }




}