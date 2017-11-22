package com.fr.plugin.chart.treemap.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.multilayer.style.VanChartMultiPieSeriesPane;
import com.fr.plugin.chart.treemap.VanChartTreeMapPlot;

import javax.swing.*;

/**
 * Created by Fangjie on 2016/6/15.
 */
public class VanChartTreeMapSeriesPane extends VanChartMultiPieSeriesPane {

    public VanChartTreeMapSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel createSeriesStylePane(double[] row, double[] col) {
        supportDrill = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Open"),
                Inter.getLocText("Plugin-ChartF_Close")});
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-ChartF_Drill"), supportDrill);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("FR-Designer-Widget_Style"), panel);
    }

    protected void populatePieAttr() {
        if(plot.accept(VanChartTreeMapPlot.class)){
            VanChartTreeMapPlot treeMapPlot = (VanChartTreeMapPlot)plot;
            supportDrill.setSelectedIndex(treeMapPlot.isSupportDrill() ? 0 : 1);
        }
    }

    @Override
    protected void updatePieAttr() {
        if(plot.accept(VanChartTreeMapPlot.class)){
            VanChartTreeMapPlot treeMapPlot = (VanChartTreeMapPlot)plot;
            treeMapPlot.setSupportDrill(supportDrill.getSelectedIndex() == 0);
        }
    }
}
