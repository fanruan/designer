package com.fr.design.chart;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.*;
import com.fr.design.chart.axis.*;
import com.fr.design.chart.series.SeriesCondition.dlp.*;
import com.fr.design.mainframe.chart.gui.style.axis.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-2
 * Time   : 上午11:27
 */
public class ChartPlotFactory {

    private static Map<String, FactoryObject> map = new HashMap<String, FactoryObject>();

    private static Map<String, Class> axisPane = new HashMap<String, Class>(){
        {
            put(ChartValuePane.class.getName(), ChartValueNoFormulaPane.class);
            put(ChartSecondValuePane.class.getName(), ChartSecondValueNoFormulaPane.class);
            put(ChartPercentValuePane.class.getName(), ChartPercentValueNoFormulaPane.class);
            put(ChartCategoryPane.class.getName(), ChartCategoryNoFormulaPane.class);
        }
    };

    static {
        map.put(AreaPlot.class.getName(), new FactoryObject()
                .setDataLabelPaneClass(AreaDataLabelPane.class));
        map.put(Area3DPlot.class.getName(), new FactoryObject()
                .setDataLabelPaneClass(AreaDataLabelPane.class));
        map.put(Bar2DPlot.class.getName(), new FactoryObject()
                .setDataLabelPaneClass(Bar2DDataLabelPane.class));
        map.put(Bar3DPlot.class.getName(), new FactoryObject()
        		.setDataLabelPaneClass(Bar2DDataLabelPane.class));
        map.put(BubblePlot.class.getName(), new FactoryObject()
                .setAxisPaneCls(XYChartStyleAxisPane.class)
                .setDataLabelPaneClass(BubbleDataLabelPane.class));
        map.put(CustomPlot.class.getName(), new FactoryObject()
                .setAxisPaneCls(CustomChartStyleAxisPane.class)
                .setDataLabelPaneClass(Bar2DDataLabelPane.class));
        map.put(GanttPlot.class.getName(), new FactoryObject()
                .setAxisPaneCls(GanntChartStyleAxisPane.class));
        map.put(LinePlot.class.getName(), new FactoryObject()
       			.setDataLabelPaneClass(LineDataLabelPane.class));
        map.put(MapPlot.class.getName(), new FactoryObject()
                .setDataLabelPaneClass(MapDataLabelPane.class));
        map.put(MeterBluePlot.class.getName(), new FactoryObject()
                .setDataLabelPaneClass(MeterDataLabelPane.class));
        map.put(MeterPlot.class.getName(), new FactoryObject()
                .setDataLabelPaneClass(MeterDataLabelPane.class));
        map.put(PiePlot.class.getName(), new FactoryObject()
                .setDataLabelPaneClass(PieDataLabelPane.class));
        map.put(Pie3DPlot.class.getName(), new FactoryObject()
        		.setDataLabelPaneClass(PieDataLabelPane.class));
        map.put(RadarPlot.class.getName(), new FactoryObject()
                .setAxisPaneCls(RadarChartStyleAxisPane.class)
                .setDataLabelPaneClass(RadarDataLabelPane.class));
        map.put(StockPlot.class.getName(), new FactoryObject()
        		.setAxisPaneCls(TernaryChartStyleAxisPane.class)
                .setDataLabelPaneClass(StockDataLabelPane.class));
        map.put(RangePlot.class.getName(), new FactoryObject()
        		.setDataLabelPaneClass(RangeDataLabelPane.class)
                .setAxisPaneCls(ValueChartStyleAxisPane.class));
        map.put(XYScatterPlot.class.getName(), new FactoryObject()
                .setAxisPaneCls(XYChartStyleAxisPane.class)
                .setDataLabelPaneClass(XYDataLabelPane.class));
        map.put(FunnelPlot.class.getName(), new FactoryObject()
                .setDataLabelPaneClass(FunnelDataLabelPane.class));
    }

    private ChartPlotFactory() {

    }

    /**
     * 创建对应的坐标轴界面
     * @param plot plot类型
     * @return 返回坐标轴界面
     */
    public static ChartStyleAxisPane createChartStyleAxisPaneByPlot(Plot plot) {
        FactoryObject factoryObject = map.get(plot.getClass().getName());
        if (factoryObject != null && factoryObject.getAxisPaneClass() != null) {
            try {
                Constructor<? extends ChartStyleAxisPane> c = factoryObject.getAxisPaneClass().getConstructor(Plot.class);
                return c.newInstance(plot);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return new BinaryChartStyleAxisPane(plot);
    }

    /**
     * 创建对应的标签Plot
     * @param plotClass plot的类
     * @return 对应的标签界面
     */
    public static DataLabelPane createDataLabelPane4Plot(Class plotClass) {
        FactoryObject factoryObject = map.get(plotClass.getName());
        if (factoryObject != null && factoryObject.getDataLabelPaneClass() != null) {
            try {
                Constructor c = factoryObject.getDataLabelPaneClass().getConstructor();
                return (DataLabelPane) c.newInstance();
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return new DataLabelPane();
    }

    public static ChartAxisUsePane getNoFormulaPane(ChartAxisUsePane pane){
        Class aClass = axisPane.get(pane.getClass().getName());
        if(aClass != null){
            try {
                Constructor c = aClass.getConstructor();
                return (ChartAxisUsePane) c.newInstance();
            }catch (Exception e){
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }

        return pane;
    }

}