package com.fr.plugin.chart.bar;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.axis.bar.VanChartBarAxisPane;

import java.util.List;

/**
 * Created by Mitisky on 16/6/8.
 */
public class VanChartBarStylePane extends VanChartStylePane {
    public VanChartBarStylePane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected void createVanChartAxisPane(List<BasicPane> paneList, VanChartAxisPlot plot) {
        paneList.add(new VanChartBarAxisPane(plot, VanChartBarStylePane.this));
    }
}
