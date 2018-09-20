package com.fr.van.chart.custom;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.PiePlot4VanChart;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.bubble.VanChartBubblePlot;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.type.CustomPlotType;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.radar.VanChartRadarPlot;
import com.fr.plugin.chart.scatter.VanChartScatterPlot;
import com.fr.van.chart.bubble.data.VanChartBubblePlotTableDataContentPane;
import com.fr.van.chart.custom.component.CustomPlotLocationPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.designer.style.axis.VanChartAxisPane;
import com.fr.van.chart.designer.style.axis.gauge.VanChartGaugeAxisPane;
import com.fr.van.chart.scatter.data.VanChartScatterPlotTableDataContentPane;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mitisky on 16/6/23.
 */
public class CustomPlotDesignerPaneFactory {

    //图表类型对应数据配置界面
    private static Map<Class<? extends Plot>, Class<? extends AbstractTableDataContentPane>> plotTableDataContentPaneMap = new HashMap<Class<? extends Plot>, Class<? extends AbstractTableDataContentPane>>();




    //图表类型对应的位置面板
    private static Map<Class<? extends Plot>, Class<? extends BasicBeanPane<Plot>>> plotPositionMap = new HashMap<Class<? extends Plot>, Class<? extends BasicBeanPane<Plot>>>();

    static {
        plotPositionMap.put(PiePlot4VanChart.class, CustomPlotLocationPane.class);
        plotPositionMap.put(VanChartRadarPlot.class, CustomPlotLocationPane.class);
        plotPositionMap.put(VanChartGaugePlot.class, CustomPlotLocationPane.class);
    }

    /**
     * 根据图表类型创建位置面板
     * @param plot 图表
     * @return 位置面板
     */
    public static BasicBeanPane<Plot> createCustomPlotPositionPane(Plot plot) {
        Class<? extends Plot> key = plot.getClass();
        if(plotPositionMap.containsKey(key)){
            try{
                Class<? extends BasicBeanPane<Plot>> cl = plotPositionMap.get(key);
                Constructor<? extends BasicBeanPane<Plot> > constructor = cl.getConstructor();
                return constructor.newInstance();
            } catch (Exception e){
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        return null;
    }


    /**
     * 每种类型对应的数据配置界面
     * @return
     */
    static {
        plotTableDataContentPaneMap.put(VanChartScatterPlot.class, VanChartScatterPlotTableDataContentPane.class);
        plotTableDataContentPaneMap.put(VanChartBubblePlot.class, VanChartBubblePlotTableDataContentPane.class);
    }

    /**
     * 根据图表类型创建数据配置
     * @param plot 图表
     * @param parent
     * @return 数据配置界面
     */
    public static AbstractTableDataContentPane createCustomPlotTableDataContentPane(Plot plot, ChartDataPane parent) {
        Class<? extends Plot> key = plot.getClass();
        if(plotTableDataContentPaneMap.containsKey(key)){
            try{
                Class<? extends AbstractTableDataContentPane> cl = plotTableDataContentPaneMap.get(key);
                Constructor<? extends AbstractTableDataContentPane > constructor = cl.getConstructor(ChartDataPane.class);
                return constructor.newInstance(parent);
            } catch (Exception e){
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        return new CategoryPlotTableDataContentPane(parent);
    }


    /**
     * plotType是否需要建立新的坐标系面板
     */
    private static Map<CustomPlotType, Class<? extends VanChartAxisPane>> diffAxisMap = new HashMap<CustomPlotType, Class<? extends VanChartAxisPane>>();
    static {
        diffAxisMap.put(CustomPlotType.POINTER_360, VanChartGaugeAxisPane.class);
        diffAxisMap.put(CustomPlotType.POINTER_180, VanChartGaugeAxisPane.class);
        diffAxisMap.put(CustomPlotType.RING, VanChartGaugeAxisPane.class);
        diffAxisMap.put(CustomPlotType.SLOT, VanChartGaugeAxisPane.class);
        diffAxisMap.put(CustomPlotType.CUVETTE, VanChartGaugeAxisPane.class);
        diffAxisMap.put(CustomPlotType.RADAR, null);//默认的为null,直接new,不用反射
        diffAxisMap.put(CustomPlotType.STACK_RADAR, null);
    }

    public static Boolean isUseDiffAxisPane(VanChartPlot plot){
        CustomPlotType customPlotType = CustomPlotFactory.getCustomType(plot);
        return diffAxisMap.containsKey(customPlotType);
    }

    public static VanChartAxisPane createAxisPane(VanChartAxisPlot plot, VanChartStylePane parent) {
        CustomPlotType key = CustomPlotFactory.getCustomType((VanChartPlot)plot);
        if(diffAxisMap.containsKey(key)){
            try{
                Class<? extends VanChartAxisPane> cl = diffAxisMap.get(key);
                if(cl != null) {
                    Constructor<? extends VanChartAxisPane> constructor = cl.getConstructor(VanChartAxisPlot.class, VanChartStylePane.class);
                    return constructor.newInstance(plot, parent);
                }
            } catch (Exception e){
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        return new VanChartAxisPane(plot,parent);
    }

}
