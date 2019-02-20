package com.fr.van.chart.designer.style.axis;

import com.fr.plugin.chart.area.VanChartAreaPlot;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.line.VanChartLinePlot;
import com.fr.plugin.chart.radar.VanChartRadarPlot;
import com.fr.van.chart.designer.style.axis.gauge.VanChartAxisScrollPaneWithGauge;
import com.fr.van.chart.designer.style.axis.radar.VanChartXAxisScrollPaneWithRadar;
import com.fr.van.chart.designer.style.axis.radar.VanChartYAxisScrollPaneWithRadar;

import java.util.HashMap;

/**
 * Created by Mitisky on 15/12/30.
 * 坐标轴界面
 */
public class AxisPaneFactory {

    private static HashMap<String, Class<? extends VanChartXYAxisPaneInterface>> XAxisPaneMap = new HashMap<String, Class<? extends VanChartXYAxisPaneInterface>>();
    private static HashMap<String, Class<? extends VanChartXYAxisPaneInterface>> YAxisPaneMap = new HashMap<String, Class<? extends VanChartXYAxisPaneInterface>>();

    private static void initXAxisPaneMap() {
        XAxisPaneMap.put(VanChartColumnPlot.class.getName(), VanChartAxisScrollPaneWithTypeSelect.class);
        XAxisPaneMap.put(VanChartLinePlot.class.getName(), VanChartAxisScrollPaneWithTypeSelect.class);
        XAxisPaneMap.put(VanChartAreaPlot.class.getName(), VanChartAxisScrollPaneWithTypeSelect.class);

        XAxisPaneMap.put(VanChartRadarPlot.class.getName(), VanChartXAxisScrollPaneWithRadar.class);
    }

    private static void initYAxisPaneMap() {
        YAxisPaneMap.put(VanChartColumnPlot.class.getName(), VanChartAxisScrollPaneWithOutTypeSelect.class);
        YAxisPaneMap.put(VanChartLinePlot.class.getName(), VanChartAxisScrollPaneWithOutTypeSelect.class);
        YAxisPaneMap.put(VanChartAreaPlot.class.getName(), VanChartAxisScrollPaneWithOutTypeSelect.class);

        YAxisPaneMap.put(VanChartGaugePlot.class.getName(), VanChartAxisScrollPaneWithGauge.class);
        YAxisPaneMap.put(VanChartRadarPlot.class.getName(), VanChartYAxisScrollPaneWithRadar.class);
    }



    public static VanChartXYAxisPaneInterface getXAxisScrollPane(VanChartAxisPlot plot) {
        if(XAxisPaneMap.isEmpty()){
            initXAxisPaneMap();
        }
        Class<? extends VanChartXYAxisPaneInterface> paneClass = XAxisPaneMap.get(plot.getClass().getName());
        return createPane(paneClass);
    }

    public static VanChartXYAxisPaneInterface getYAxisScrollPane(VanChartAxisPlot plot) {
        if(YAxisPaneMap.isEmpty()){
            initYAxisPaneMap();
        }
        Class<? extends VanChartXYAxisPaneInterface> paneClass = YAxisPaneMap.get(plot.getClass().getName());
        return createPane(paneClass);
    }

    private static VanChartXYAxisPaneInterface createPane(Class<? extends VanChartXYAxisPaneInterface> paneClass) {
        if(paneClass == null){
            return null;
        }
        try {
            return paneClass.newInstance();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return null;
        }
    }

}