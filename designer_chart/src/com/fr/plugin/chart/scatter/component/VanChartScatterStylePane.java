package com.fr.plugin.chart.scatter.component;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.axis.scatter.VanChartDoubleValueAxisPane;

import java.util.List;

public class VanChartScatterStylePane extends VanChartStylePane {

    private static final long serialVersionUID = 186776958263021761L;

    public VanChartScatterStylePane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected void createVanChartAxisPane(List<BasicPane> paneList, VanChartAxisPlot plot) {
        paneList.add(new VanChartDoubleValueAxisPane(plot, VanChartScatterStylePane.this));
    }

}