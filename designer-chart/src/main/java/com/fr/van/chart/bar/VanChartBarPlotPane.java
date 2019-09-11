package com.fr.van.chart.bar;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.bar.BarIndependentVanChart;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/10/20.
 */
public class VanChartBarPlotPane extends AbstractVanChartTypePane {

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/bar/images/bar.png",
                "/com/fr/van/chart/bar/images/stack.png",
                "/com/fr/van/chart/bar/images/percentstack.png",
                "/com/fr/van/chart/bar/images/custom.png",
        };
    }

    protected Plot getSelectedClonedPlot(){
        VanChartColumnPlot newPlot = null;
        Chart[] barChart = BarIndependentVanChart.BarVanChartTypes;
        for(int i = 0, len = barChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartColumnPlot)barChart[i].getPlot();
            }
        }
        Plot cloned = null;
        try {
            if(newPlot != null) {
                cloned = (Plot) newPlot.clone();
            }
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error In ColumnChart");
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return BarIndependentVanChart.BarVanChartTypes[0];
    }

}