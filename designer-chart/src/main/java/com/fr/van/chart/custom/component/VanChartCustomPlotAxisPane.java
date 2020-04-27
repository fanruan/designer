package com.fr.van.chart.custom.component;

import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.custom.CustomPlotDesignerPaneFactory;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.designer.style.axis.VanChartAxisPane;

import java.util.List;

/**
 * Created by Fangjie on 2016/5/12.
 */
public class VanChartCustomPlotAxisPane extends VanChartAxisPane {

    public VanChartCustomPlotAxisPane(VanChartAxisPlot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    //删除此坐标轴相关堆积属性的设置
    protected void removeOthers(int axisIndex, boolean isXAxis){
        //堆积和坐标轴

        VanChartCustomPlot customPlot = (VanChartCustomPlot)editingPlot;
        List<VanChartPlot> plotList = customPlot.getCustomPlotList();
        for (int k = 0; k < plotList.size(); k++) {
            if (customPlot.getStandardAxisOrder().contains(k)){
                VanChartRectanglePlot vanChartPlot = (VanChartRectanglePlot) plotList.get(k);
                if (vanChartPlot.isHaveAxis() && !CustomPlotDesignerPaneFactory.isUseDiffAxisPane(vanChartPlot)){
                    remove(axisIndex, isXAxis, vanChartPlot);
                }
            }
        }
    }

    private void remove(int axisIndex, boolean isXAxis, VanChartRectanglePlot vanChartPlot){
        ConditionCollection stackAndAxisCondition = vanChartPlot.getStackAndAxisCondition();
        if (stackAndAxisCondition == null) {
            return;
        }
        for (int i = 0, len = stackAndAxisCondition.getConditionAttrSize(); i < len; i++) {
            ConditionAttr conditionAttr = stackAndAxisCondition.getConditionAttr(i);
            AttrSeriesStackAndAxis stackAndAxis = (AttrSeriesStackAndAxis) conditionAttr.getExisted(AttrSeriesStackAndAxis.class);
            int index = isXAxis ? stackAndAxis.getXAxisIndex() : stackAndAxis.getYAxisIndex();
            if (index == axisIndex) {
                stackAndAxisCondition.removeConditionAttr(conditionAttr);
            }
        }
    }

    @Override
    protected void updateBuryingPoint(VanChart chart) {
    }
}
