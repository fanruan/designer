package com.fr.plugin.chart.designer.style.axis.scatter;

import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.axis.VanChartAxisPane;
import com.fr.plugin.chart.designer.style.axis.VanChartAxisScrollPaneWithOutTypeSelect;
import com.fr.plugin.chart.designer.style.axis.VanChartXYAxisPaneInterface;
import com.fr.plugin.chart.designer.style.axis.bar.VanChartXAxisScrollPaneWithOutTypeSelect;

/**
 * 样式-坐标轴界面
 * 双值轴
 * 坐标轴name直接用默认的 "X轴" "Y轴"
 */
public class VanChartDoubleValueAxisPane extends VanChartAxisPane {

    public VanChartDoubleValueAxisPane(VanChartAxisPlot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    @Override
    protected VanChartXYAxisPaneInterface getDefaultXAxisScrollPane() {
        return new VanChartXAxisScrollPaneWithOutTypeSelect();
    }

    @Override
    protected VanChartXYAxisPaneInterface getDefaultYAxisScrollPane() {
        return new VanChartAxisScrollPaneWithOutTypeSelect();
    }

    @Override
    protected void update(VanChartRectanglePlot rectanglePlot) {
        if(rectanglePlot == null){
            return;
        }

        for(VanChartAxis axis : rectanglePlot.getXAxisList()){
            VanChartXYAxisPaneInterface axisPane = xAxisPaneMap.get(axis.getAxisName());
            axisPane.update(axis);
        }

        for(VanChartAxis axis : rectanglePlot.getYAxisList()){
            VanChartXYAxisPaneInterface axisPane = yAxisPaneMap.get(axis.getAxisName());
            axisPane.update(axis);
        }
    }
}