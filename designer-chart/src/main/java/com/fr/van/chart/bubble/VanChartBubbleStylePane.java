package com.fr.van.chart.bubble;

import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.style.series.ChartSeriesPane;
import com.fr.plugin.chart.bubble.VanChartBubblePlot;
import com.fr.van.chart.bubble.force.VanChartForceBubbleAreaPane;
import com.fr.van.chart.bubble.force.VanChartForceBubbleLabelPane;
import com.fr.van.chart.bubble.force.VanChartForceBubbleTooltipPane;
import com.fr.van.chart.designer.style.background.VanChartAreaPane;
import com.fr.van.chart.scatter.component.VanChartScatterStylePane;

import java.util.List;

/**
 * Created by Mitisky on 16/3/31.
 */
public class VanChartBubbleStylePane extends VanChartScatterStylePane {

    public VanChartBubbleStylePane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected void createVanChartLabelPane(List<BasicPane> paneList) {
        Plot plot = getChart().getPlot();

        if(((VanChartBubblePlot) plot).isForceBubble()){
            paneList.add(new VanChartForceBubbleLabelPane(VanChartBubbleStylePane.this));
        } else {
            super.createVanChartLabelPane(paneList);
        }
    }

    protected void addVanChartTooltipPane(List<BasicPane> paneList){
        Plot plot = getChart().getPlot();
        if(((VanChartBubblePlot) plot).isForceBubble()){
            paneList.add(new VanChartForceBubbleTooltipPane(VanChartBubbleStylePane.this));
        } else {
            super.addVanChartTooltipPane(paneList);
        }
    }

    @Override
    protected void addVanChartAreaPane(List<BasicPane> paneList) {
        if (((VanChartBubblePlot)getChart().getPlot()).isForceBubble()){
            paneList.add(new VanChartForceBubbleAreaPane(getChart().getPlot(), VanChartBubbleStylePane.this));
        }else {
            paneList.add(new VanChartAreaPane(getChart().getPlot(), VanChartBubbleStylePane.this));
        }
    }

    @Override
    protected ChartSeriesPane createChartSeriesPane() {
        return new ChartSeriesPane(VanChartBubbleStylePane.this);
    }
}
