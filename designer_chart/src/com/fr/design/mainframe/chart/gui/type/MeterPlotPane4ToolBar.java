package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.FRContext;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.MeterIndependentChart;
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
 * Time: 下午2:08
 */
public class MeterPlotPane4ToolBar extends PlotPane4ToolBar {

    private static final int METER = 0;
    private static final int BLUE_METER = 1;
    private static final int SIMPLE_METER = 2;

    public MeterPlotPane4ToolBar(ChartDesigner designer) {
        super(designer);
    }

    @Override
    protected String getTypeIconPath() {
        return "com/fr/design/images/toolbar/meter/";
    }

    @Override
    protected List<ChartDesignerImagePane> initDemoList() {
        List<ChartDesignerImagePane> demoList = new ArrayList<ChartDesignerImagePane>();
        ChartDesignerImagePane pane = new ChartDesignerImagePane(getTypeIconPath(), METER, Inter.getLocText("FR-Chart-Type_Meter"),this);
        pane.setSelected(true);
        demoList.add(pane);
        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), BLUE_METER, Inter.getLocText("FR-Chart-Type_Meter")+1,this));
        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), SIMPLE_METER, Inter.getLocText("FR-Chart-Type_Meter")+2,this));
        return demoList;
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] barChart = MeterIndependentChart.meterChartTypes;
        MeterPlot newPlot = (MeterPlot) barChart[this.getSelectedIndex()].getPlot();
        setChartFontAttr4MeterStyle(newPlot);
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In MeterChart");
        }
        return cloned;
    }

    /**
     * 设置一些几本的属性
     * @param plot 绘图区对象
     */
	public static void setChartFontAttr4MeterStyle(MeterPlot plot) {
        if(plot.getMeterStyle() != null){
            plot.getMeterStyle().setTitleTextAttr(new TextAttr(FRContext.getDefaultValues().getFRFont()));
        }
	}
}