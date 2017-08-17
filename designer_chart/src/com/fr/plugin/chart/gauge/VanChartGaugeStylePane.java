package com.fr.plugin.chart.gauge;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.axis.gauge.VanChartGaugeAxisPane;

import java.util.List;

/**
 * Created by Mitisky on 16/6/7.
 */
public class VanChartGaugeStylePane extends VanChartStylePane {
    public VanChartGaugeStylePane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected void createVanChartAxisPane(List<BasicPane> paneList, VanChartAxisPlot plot) {
        paneList.add(new VanChartGaugeAxisPane(plot, VanChartGaugeStylePane.this));
    }
}
