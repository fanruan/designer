package com.fr.van.chart.custom.style;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.van.chart.custom.CustomPlotDesignerPaneFactory;
import com.fr.van.chart.designer.style.background.VanChartAxisAreaPane;

import java.util.List;


/**
 * Created by Fangjie on 2016/5/19.
 */
public class VanChartCustomAxisAreaPane extends VanChartAxisAreaPane {


    @Override
    public void updateBean(Plot plot){
        VanChartCustomPlot customPlot = (VanChartCustomPlot)plot;

        super.updateBean(customPlot);

        List<VanChartPlot> vanChartPlotList = customPlot.getCustomPlotList();

        //更新后同步坐标轴
        CustomPlotFactory.axisSynchronization(customPlot);

        //使用其他坐标轴的图形
        for (int i = 0; i < vanChartPlotList.size(); i++){
            if (vanChartPlotList.get(i).isSupportPlotBackground() && CustomPlotDesignerPaneFactory.isUseDiffAxisPane(vanChartPlotList.get(i))){
                super.updateBean(vanChartPlotList.get(i));
            }
        }
    }


    public void populateBean(Plot plot){
        VanChartCustomPlot customPlot = (VanChartCustomPlot)plot;

        if (customPlot.isHaveStandardAxis()){
            super.populateBean(customPlot);
        } else {
            List<VanChartPlot> vanChartPlotList = customPlot.getCustomPlotList();
            //使用其他坐標軸的圖形
            for (int i = 0; i < vanChartPlotList.size(); i++) {
                if (vanChartPlotList.get(i).isSupportPlotBackground() && CustomPlotDesignerPaneFactory.isUseDiffAxisPane(vanChartPlotList.get(i))) {
                    super.populateBean(vanChartPlotList.get(i));
                }
            }
        }
    }
}
