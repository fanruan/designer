package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.FRContext;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.BubbleIndependentChart;
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
 * Time: 下午1:59
 */
public class BubblePlotPane4ToolBar extends PlotPane4ToolBar {

    private static final int BUBBLE_CHART = 0;

    public BubblePlotPane4ToolBar(ChartDesigner designer) {
        super(designer);
    }

    @Override
    protected String getTypeIconPath() {
        return "com/fr/design/images/toolbar/bubble/";
    }

    @Override
    protected List<ChartDesignerImagePane> initDemoList() {
        List<ChartDesignerImagePane> demoList = new ArrayList<ChartDesignerImagePane>();
        ChartDesignerImagePane pane = new ChartDesignerImagePane(getTypeIconPath(), BUBBLE_CHART, Inter.getLocText("FR-Chart-Chart_BubbleChart"),this);
        pane.setSelected(true);
        demoList.add(pane);
        return demoList;
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] barChart = BubbleIndependentChart.bubbleChartTypes;
        BubblePlot newPlot = (BubblePlot) barChart[this.getSelectedIndex()].getPlot();
        setChartFontAttr(newPlot);
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In BubbleChart");
        }
        return cloned;
    }

    /**
     * 设置一些几本的属性
     * @param plot 绘图区对象
     */
	public static void setChartFontAttr(BubblePlot plot) {
		if (plot.getxAxis() != null) {
			TextAttr categoryTextAttr = new TextAttr();
			categoryTextAttr.setFRFont(FRContext.getDefaultValues().getFRFont());
			plot.getxAxis().setTextAttr(categoryTextAttr);
		}
		if (plot.getyAxis() != null) {
			TextAttr valueTextAttr = new TextAttr();
			valueTextAttr.setFRFont(FRContext.getDefaultValues().getFRFont());
			plot.getyAxis().setTextAttr(valueTextAttr);
		}
	}
}