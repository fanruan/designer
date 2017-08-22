package com.fr.plugin.chart.map.designer.other;

import com.fr.chart.chartattr.Plot;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.designer.PlotFactory;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.other.AutoRefreshPane;
import com.fr.plugin.chart.designer.other.AutoRefreshPaneWithoutTooltip;
import com.fr.plugin.chart.designer.other.VanChartInteractivePaneWithMapZoom;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.*;

/**
 * Created by hufan on 2016/12/20.
 */
public class VanChartMapInteractivePane extends VanChartInteractivePaneWithMapZoom {
    private static final int HYPERLINK_LEFT_GAP = 36;
    private VanChartMapHyperLinkPane hyperlinkPane;
    @Override
    protected JPanel createHyperlinkPane() {
        hyperlinkPane = new VanChartMapHyperLinkPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("M_Insert-Hyperlink"), hyperlinkPane);
    }
    @Override
    protected void populateHyperlink(Plot plot) {
        hyperlinkPane.populateBean(plot);
    }

    @Override
    protected void updateHyperlink(Plot plot) {
        hyperlinkPane.updateBean(plot);
    }

    @Override
    protected boolean largeModel(Plot plot) {
        if(plot instanceof VanChartMapPlot){
            VanChartMapPlot mapPlot = (VanChartMapPlot)plot;
            switch (mapPlot.getMapType()){
                case LINE:
                    return PlotFactory.lineMapLargeModel(mapPlot);
                default:
                    return PlotFactory.largeDataModel(mapPlot);
            }
        }
        return false;
    }

    protected AutoRefreshPane getMoreLabelPane(VanChartPlot plot) {
        boolean isLargeModel = largeModel(plot);
        VanChartMapPlot mapPlot = (VanChartMapPlot)plot;
        if (mapPlot.getMapType().equals(MapType.LINE)) {
            return new AutoRefreshPaneWithoutTooltip((VanChart) chart, isLargeModel);
        }
        return new AutoRefreshPane((VanChart) chart, isLargeModel);
    }

}