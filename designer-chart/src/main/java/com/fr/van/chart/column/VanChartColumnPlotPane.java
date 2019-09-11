package com.fr.van.chart.column;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.column.ColumnIndependentVanChart;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/9/24.
 */
public class VanChartColumnPlotPane extends AbstractVanChartTypePane {

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/column/images/column.png",
                "/com/fr/van/chart/column/images/stack.png",
                "/com/fr/van/chart/column/images/percentstack.png",
                "/com/fr/van/chart/column/images/custom.png",
        };
    }

    protected Plot getSelectedClonedPlot(){
        VanChartColumnPlot newPlot = null;
        Chart[] columnChart = ColumnIndependentVanChart.ColumnVanChartTypes;
        for(int i = 0, len = columnChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartColumnPlot)columnChart[i].getPlot();
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
        return ColumnIndependentVanChart.ColumnVanChartTypes[0];
    }

}