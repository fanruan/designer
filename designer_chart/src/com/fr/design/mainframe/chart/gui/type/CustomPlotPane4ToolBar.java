package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.CustomIndependentChart;
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
 * Time: 下午1:39
 */
public class CustomPlotPane4ToolBar extends PlotPane4ToolBar {
    private static final int CUSTOM_NO_SHEET = 0;


    public CustomPlotPane4ToolBar(ChartDesigner designer) {
        super(designer);
    }

    @Override
    protected String getTypeIconPath() {
        return "com/fr/design/images/toolbar/custom/";
    }

    @Override
    protected List<ChartDesignerImagePane> initDemoList() {
        List<ChartDesignerImagePane> demoList = new ArrayList<ChartDesignerImagePane>();
        ChartDesignerImagePane pane = new ChartDesignerImagePane(getTypeIconPath(), CUSTOM_NO_SHEET, Inter.getLocText("ChartF-Comb_Chart"),this);
        pane.setSelected(true);
        demoList.add(pane);
        return demoList;
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] barChart = CustomIndependentChart.combChartTypes;
        CustomPlot newPlot = (CustomPlot) barChart[this.getSelectedIndex()].getPlot();
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In CustomChart");
        }
        return cloned;
    }


}