package com.fr.van.chart.custom.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.van.chart.custom.component.VanChartCustomAreaBackgroundPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.designer.style.background.VanChartAreaPane;

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
