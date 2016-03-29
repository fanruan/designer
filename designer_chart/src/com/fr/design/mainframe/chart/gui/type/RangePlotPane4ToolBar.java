package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.RangeIndependentChart;
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
 * Time: 下午2:13
 */
public class RangePlotPane4ToolBar extends PlotPane4ToolBar {

    private static final int RANGE = 0;

    public RangePlotPane4ToolBar(ChartDesigner designer) {
        super(designer);
    }


    @Override
    protected String getTypeIconPath() {
        return "com/fr/design/images/toolbar/range/";
    }

    @Override
    protected List<ChartDesignerImagePane> initDemoList() {
        List<ChartDesignerImagePane> demoList = new ArrayList<ChartDesignerImagePane>();
        ChartDesignerImagePane pane = new ChartDesignerImagePane(getTypeIconPath(), RANGE, Inter.getLocText("ChartF-Range_Chart"),this);
        pane.setSelected(true);
        demoList.add(pane);
        return demoList;
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] barChart = RangeIndependentChart.rangeChartTypes;
        RangePlot newPlot = (RangePlot) barChart[this.getSelectedIndex()].getPlot();
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In RangeChart");
        }
        return cloned;
    }
}