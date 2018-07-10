package com.fr.design.chart.series.SeriesCondition;

import com.fr.design.chart.series.SeriesCondition.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : ����3:16
 */
public class ChartConditionPaneFactory {
    private static Map<String, Class<? extends ChartConditionPane>> map = new HashMap<String, Class<? extends ChartConditionPane>>();

    static {
        map.put(GanttPlotDataSeriesConditionPane.class.getName(), GanttPlotChartConditionPane.class);
        map.put(MapPlotDataSeriesConditionPane.class.getName(), MapPlotChartConditionPane.class);
        map.put(PiePlotDataSeriesConditionPane.class.getName(), PiePlotChartConditionPane.class);
        map.put(Pie3DPlotDataSeriesConditionPane.class.getName(),PiePlotChartConditionPane.class);
        map.put(XYScatterPlotDataSeriesConditionPane.class.getName(), XYPlotChartConditionPane.class);
        map.put(BubblePlotDataSeriesConditionPane.class.getName(), BubblePlotChartConditionPane.class);
    }

    private ChartConditionPaneFactory() {

    }

    /**
     * �������ɶ�Ӧ��������.
     * @param clazz ��Ӧ����
     * @return ������������.
     */
    public static ChartConditionPane createChartConditionPane(Class clazz) {
        String plotClsName = clazz.getName();
        Class<? extends ChartConditionPane> cls = map.get(plotClsName);
        cls = cls != null ? cls : ChartConditionPane.class;
        try {
            return cls.newInstance();
        } catch (Exception e) {
            return new ChartConditionPane();
        }
    }
}
