package com.fr.plugin.chart.bubble.force;

import com.fr.chart.chartattr.Plot;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;


import com.fr.plugin.chart.designer.style.VanChartStylePane;

import com.fr.plugin.chart.designer.style.background.VanChartAreaPane;


/**
 * 属性表, 图表样式-背景界面.
 */
public class VanChartForceBubbleAreaPane extends VanChartAreaPane {

    public VanChartForceBubbleAreaPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    @Override
    protected void initPlotPane(boolean b, AbstractAttrNoScrollPane parent) {
        plotPane = new VanChartForceBubbleAreaBackgroundPane(true, parent);
    }
}