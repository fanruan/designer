package com.fr.van.chart.gantt.designer.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.chartx.data.GanttChartDataDefinition;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.gantt.GanttIndependentVanChart;
import com.fr.plugin.chart.gantt.VanChartGanttPlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by hufan on 2017/1/9.
 */
public class VanChartGanttPlotPane extends AbstractVanChartTypePane {
    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/gantt/images/gantt.png"
        };
    }

    public Chart getDefaultChart() {
        return GanttIndependentVanChart.ganttVanChartTypes[0];
    }

    protected Plot getSelectedClonedPlot() {
        Chart chart = getDefaultChart();
        VanChartGanttPlot newPlot = (VanChartGanttPlot) chart.getPlot();

        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return cloned;
    }

    @Override
    protected boolean acceptDefinition(ChartDataDefinitionProvider definition, VanChartPlot vanChartPlot) {
        return definition instanceof GanttChartDataDefinition;
    }
}