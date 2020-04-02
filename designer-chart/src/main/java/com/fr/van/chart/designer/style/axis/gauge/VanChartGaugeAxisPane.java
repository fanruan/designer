package com.fr.van.chart.designer.style.axis.gauge;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.designer.style.axis.VanChartAxisPane;

import java.awt.BorderLayout;

/**
 * Created by Mitisky on 16/6/7.
 * 仪表盘的坐标轴界面
 */
public class VanChartGaugeAxisPane extends VanChartAxisPane {
    private VanChartAxisScrollPaneWithGauge gaugeAxisPane;

    public VanChartGaugeAxisPane(VanChartAxisPlot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    @Override
    protected void initComponents() {
        this.setLayout(new BorderLayout());
        gaugeAxisPane = new VanChartAxisScrollPaneWithGauge();
        this.add(gaugeAxisPane, BorderLayout.CENTER);

        gaugeAxisPane.setParentPane(parent);
    }

    @Override
    protected void populate(){
        if(editingPlot == null){
            return;
        }
        if(editingPlot instanceof VanChartGaugePlot) {
            gaugeAxisPane.populateBean((VanChartGaugePlot)editingPlot, parent);
        }
    }

    @Override
    public void updateBean(Plot plot) {
        if(plot == null){
            return;
        }
        if(plot instanceof VanChartGaugePlot) {
            gaugeAxisPane.update(((VanChartGaugePlot)plot).getGaugeAxis());
        }
    }
}
