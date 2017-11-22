package com.fr.plugin.chart.custom.component;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.plugin.chart.custom.style.VanChartCustomAxisAreaPane;
import com.fr.plugin.chart.designer.style.background.VanChartAreaBackgroundPane;
import com.fr.plugin.chart.designer.style.background.VanChartAxisAreaPane;

/**
 * Created by Fangjie on 2016/5/19.
 */
public class VanChartCustomAreaBackgroundPane extends VanChartAreaBackgroundPane {
    public VanChartCustomAreaBackgroundPane(boolean isPlot, AbstractAttrNoScrollPane parent) {
        super(isPlot, parent);
    }

    protected VanChartAxisAreaPane initAxisAreaPane() {
        return new VanChartCustomAxisAreaPane();
    }
}
