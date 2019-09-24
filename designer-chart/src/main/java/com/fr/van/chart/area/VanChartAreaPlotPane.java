package com.fr.van.chart.area;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.area.AreaIndependentVanChart;
import com.fr.plugin.chart.area.VanChartAreaPlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/11/18.
 */
public class VanChartAreaPlotPane extends AbstractVanChartTypePane {

    @Override
    protected String[] getTypeIconPath() {

        return new String[]{"/com/fr/van/chart/area/images/area.png",
                "/com/fr/van/chart/area/images/stack.png",
                "/com/fr/van/chart/area/images/percentStack.png",
                "/com/fr/van/chart/area/images/custom.png",
        };
    }

    protected Plot getSelectedClonedPlot() {
        VanChartAreaPlot newPlot = null;
        Chart[] areaChart = AreaIndependentVanChart.AreaVanChartTypes;
        for (int i = 0, len = areaChart.length; i < len; i++) {
            if (typeDemo.get(i).isPressing) {
                newPlot = (VanChartAreaPlot) areaChart[i].getPlot();
            }
        }
        Plot cloned = null;
        if(newPlot != null) {
            try {
                cloned = (Plot) newPlot.clone();
            } catch (CloneNotSupportedException e) {
                FineLoggerFactory.getLogger().error("Error In AreaChart");
            }
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return AreaIndependentVanChart.AreaVanChartTypes[0];
    }

}