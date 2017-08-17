package com.fr.plugin.chart.custom.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.plugin.chart.custom.component.VanChartCustomAreaBackgroundPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.background.VanChartAreaPane;

/**
 * Created by Fangjie on 2016/5/19.
 */
public class VanChartCustomAreaPane extends VanChartAreaPane {
    public VanChartCustomAreaPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    @Override
    protected void initPlotPane(boolean b, AbstractAttrNoScrollPane parent) {
        plotPane = new VanChartCustomAreaBackgroundPane(true, parent);
    }
}
