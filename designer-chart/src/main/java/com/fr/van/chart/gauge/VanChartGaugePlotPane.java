package com.fr.van.chart.gauge;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.gauge.GaugeIndependentVanChart;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/11/27.
 */
public class VanChartGaugePlotPane extends AbstractVanChartTypePane {

    private static final long serialVersionUID = -4599483879031804911L;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/gauge/images/pointer.png",
                "/com/fr/van/chart/gauge/images/pointer_180.png",
                "/com/fr/van/chart/gauge/images/ring.png",
                "/com/fr/van/chart/gauge/images/slot.png",
                "/com/fr/van/chart/gauge/images/cuvette.png",
        };
    }

    protected Plot getSelectedClonedPlot(){
        VanChartGaugePlot newPlot = null;
        Chart[] GaugeChart = GaugeIndependentVanChart.GaugeVanChartTypes;
        for(int i = 0, len = GaugeChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartGaugePlot)GaugeChart[i].getPlot();
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
            FineLoggerFactory.getLogger().error("Error In GaugeChart");
        }
        return cloned;
    }

    /**
     * 保存界面属性
     */
    public void updateBean(Chart chart) {
        boolean oldISMulti = chart.getPlot() instanceof VanChartGaugePlot && ((VanChartGaugePlot)chart.getPlot()).isMultiPointer();
        super.updateBean(chart);
        boolean newISMulti = chart.getPlot() instanceof VanChartGaugePlot && ((VanChartGaugePlot)chart.getPlot()).isMultiPointer();
        if(oldISMulti != newISMulti){
            chart.setFilterDefinition(null);
        }
    }

    protected void cloneOldConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
    }

    public Chart getDefaultChart() {
        return GaugeIndependentVanChart.GaugeVanChartTypes[0];
    }

    @Override
    protected void cloneHotHyperLink(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException {
        if(oldPlot instanceof VanChartGaugePlot && newPlot instanceof VanChartGaugePlot){
            if(((VanChartGaugePlot) oldPlot).isMultiPointer() == ((VanChartGaugePlot) newPlot).isMultiPointer()){
                super.cloneHotHyperLink(oldPlot, newPlot);
            }
        }
    }
}