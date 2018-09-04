package com.fr.van.chart.gantt.designer.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;

import com.fr.plugin.chart.gantt.GanttIndependentVanChart;
import com.fr.plugin.chart.gantt.VanChartGanttPlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by hufan on 2017/1/9.
 */
public class VanChartGanttPlotPane extends AbstractVanChartTypePane {
    public static final String TITLE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_New_Gantt");
    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/gantt/images/gantt.png"
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Gantt_Chart")
        };
    }

    @Override
    protected String getPlotTypeID() {
        return VanChartGanttPlot.VAN_CHART_GANTT_PLOT_ID;
    }

    /**
     * 返回界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_New_Gantt");
    }

    public Chart getDefaultChart() {
        return GanttIndependentVanChart.ganttVanChartTypes[0];
    }

    protected Plot getSelectedClonedPlot(){
        Chart chart = getDefaultChart();
        VanChartGanttPlot newPlot = (VanChartGanttPlot) chart.getPlot();

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return cloned;
    }
}