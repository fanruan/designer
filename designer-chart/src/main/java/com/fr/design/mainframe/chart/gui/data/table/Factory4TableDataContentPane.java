package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.GanttPlot;
import com.fr.chart.chartattr.MeterBluePlot;
import com.fr.chart.chartattr.MeterPlot;
import com.fr.chart.chartattr.Pie3DPlot;
import com.fr.chart.chartattr.PiePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.SimpleMeterPlot;
import com.fr.chart.chartattr.StockPlot;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.log.FineLoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-23
 * Time: 上午11:08
 */
public class Factory4TableDataContentPane {

    private static Map<Class<? extends Plot>, Class<? extends AbstractTableDataContentPane>> map = new HashMap<Class<? extends Plot>, Class<? extends AbstractTableDataContentPane>>();

    static {
        map.put(PiePlot.class, PiePlotTableDataContentPane.class);
        map.put(Pie3DPlot.class,PiePlotTableDataContentPane.class);
        map.put(BubblePlot.class, BubblePlotTableDataContentPane.class);
        map.put(XYScatterPlot.class, XYScatterPlotTableDataContentPane.class);
        map.put(GanttPlot.class, GanttPlotTableDataContentPane.class);
        map.put(StockPlot.class, StockPlotTableDataContentPane.class);
        map.put(MeterPlot.class,MeterPlotTableDataContentPane.class);
        map.put(MeterBluePlot.class,MeterPlotTableDataContentPane.class);
        map.put(SimpleMeterPlot.class,MeterPlotTableDataContentPane.class);
    }

    /**
     * 根据plot生产对应的数据面板
     * @param plot  图表
     * @param parentPane 父面板
     * @return 对应的数据面板
     */
    public static AbstractTableDataContentPane createTableDataContenetPaneWithPlotType(Plot plot, ChartDataPane parentPane){
        try {
            Class<? extends AbstractTableDataContentPane> classname = map.get(plot.getClass());
            if(classname == null){
                return new CategoryPlotTableDataContentPane(parentPane);
            }
            Constructor<? extends AbstractTableDataContentPane> tabledataPaneClass = classname.getConstructor(ChartDataPane.class);
            if(tabledataPaneClass !=null){
                return tabledataPaneClass.newInstance(parentPane);
            }else{
                return new CategoryPlotTableDataContentPane(parentPane);
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return new CategoryPlotTableDataContentPane(parentPane);
    }

}