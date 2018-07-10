package com.fr.van.chart.bubble.force;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.van.chart.designer.style.background.VanChartAreaBackgroundPane;

import java.awt.Component;

//图表区|绘图区 边框和背景
public class VanChartForceBubbleAreaBackgroundPane extends VanChartAreaBackgroundPane {


    public VanChartForceBubbleAreaBackgroundPane(boolean isPlot, AbstractAttrNoScrollPane parent) {
        super(isPlot, parent);
    }

    @Override
    protected Component[][] initComponents() {
        return new Component[][]{
                new Component[]{chartBorderPane},
                new Component[]{chartBackgroundPane},
        };
    }
}