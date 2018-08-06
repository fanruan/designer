package com.fr.van.chart.map.designer.other;

import com.fr.chart.chartattr.Plot;

import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.other.AutoRefreshPane;
import com.fr.van.chart.designer.other.AutoRefreshPaneWithoutTooltip;
import com.fr.van.chart.designer.other.VanChartInteractivePaneWithMapZoom;

import javax.swing.JPanel;

/**
 * Created by hufan on 2016/12/20.
 */
public class VanChartMapInteractivePane extends VanChartInteractivePaneWithMapZoom {
    private static final int HYPERLINK_LEFT_GAP = 36;
    private VanChartMapHyperLinkPane hyperlinkPane;
    @Override
    protected JPanel createHyperlinkPane() {
        hyperlinkPane = new VanChartMapHyperLinkPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_M_Insert_Hyperlink"), hyperlinkPane);
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