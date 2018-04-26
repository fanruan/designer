package com.fr.van.chart.drillmap.designer.other;

import com.fr.chart.chartattr.Plot;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.van.chart.custom.component.VanChartHyperLinkPane;

/**
 * Created by hufan on 2016/12/13.
 */
public class VanChartCatalogHyperLinkPane extends VanChartHyperLinkPane {

    protected void updateHotHyperLink(Plot plot, NameJavaScriptGroup nameGroup) {
        if (plot instanceof VanChartDrillMapPlot) {
            ((VanChartDrillMapPlot) plot).setDrillUpHyperLink(nameGroup);
        }
    }

    protected NameJavaScriptGroup populateHotHyperLink(Plot plot) {
        if (plot instanceof VanChartDrillMapPlot) {
            return ((VanChartDrillMapPlot) plot).getDrillUpHyperLink();
        }

        return null;
    }
}
