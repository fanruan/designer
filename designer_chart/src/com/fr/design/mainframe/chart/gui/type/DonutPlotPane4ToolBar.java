package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.DonutIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-29
 * Time: 下午2:18
 */
public class DonutPlotPane4ToolBar extends PlotPane4ToolBar {

    private static final int DONUT_CHART = 0; //2d圆环图
   	private static final int THREE_D_DONUT_CHART = 1; //3D圆环图

    public DonutPlotPane4ToolBar(ChartDesigner designer) {
        super(designer);
    }

    @Override
    protected String getTypeIconPath() {
        return "com/fr/design/images/toolbar/donut/";
    }

    @Override
    protected List<ChartDesignerImagePane> initDemoList() {
        List<ChartDesignerImagePane> demoList = new ArrayList<ChartDesignerImagePane>();
        ChartDesignerImagePane pane = new ChartDesignerImagePane(getTypeIconPath(), DONUT_CHART, Inter.getLocText("FR-Chart-Type_Donut"),this);
        pane.setSelected(true);
        demoList.add(pane);
        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), THREE_D_DONUT_CHART, Inter.getLocText(new String[]{"FR-Chart-Chart_3D", "FR-Chart-Type_Donut"}),this));
        return demoList;
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] barChart = DonutIndependentChart.donutChartTypes;
        DonutPlot newPlot = (DonutPlot) barChart[this.getSelectedIndex()].getPlot();
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In DonutChart");
        }
        return cloned;
    }
}