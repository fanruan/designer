package com.fr.van.chart.line;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.line.LineIndependentVanChart;
import com.fr.plugin.chart.line.VanChartLinePlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/11/5.
 */
public class VanChartLinePlotPane extends AbstractVanChartTypePane {

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/line/images/line.png",
                "/com/fr/van/chart/line/images/stack.png",
                "/com/fr/van/chart/line/images/custom.png",
        };
    }

    protected Plot getSelectedClonedPlot(){
        VanChartLinePlot newPlot = null;
        Chart[] lineChart = LineIndependentVanChart.LineVanChartTypes;
        for(int i = 0, len = lineChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartLinePlot)lineChart[i].getPlot();
            }
        }
        Plot cloned = null;
        try {
            if (newPlot == null) {
                throw new IllegalArgumentException("newPlot con not be null");
            }else {
                cloned = (Plot)newPlot.clone();
            }
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error In LineChart");
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return LineIndependentVanChart.LineVanChartTypes[0];
    }
}