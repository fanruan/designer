package com.fr.plugin.chart.designer.style.axis.gauge;

import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.axis.VanChartAxisScrollPaneWithOutTypeSelect;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;

/**
 * 这边只是加一层scroll.仪表盘的坐标轴，数值轴。
 */
public class VanChartAxisScrollPaneWithGauge extends VanChartAxisScrollPaneWithOutTypeSelect{
    private static final long serialVersionUID = 7069253678757456911L;

    protected void initAxisPane() {
        axisPane = new VanChartGaugeDetailAxisPane();
    }

    public void populateBean(VanChartGaugePlot gaugePlot, VanChartStylePane parent){
        if(axisPane instanceof VanChartGaugeDetailAxisPane){
            ((VanChartGaugeDetailAxisPane)axisPane).populateBean(gaugePlot, parent);
        }
    }

}