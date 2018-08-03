package com.fr.van.chart.treemap.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

import com.fr.plugin.chart.treemap.VanChartTreeMapPlot;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.multilayer.style.VanChartMultiPieSeriesPane;

import javax.swing.JPanel;

/**
 * Created by Fangjie on 2016/6/15.
 */
public class VanChartTreeMapSeriesPane extends VanChartMultiPieSeriesPane {

    public VanChartTreeMapSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel createSeriesStylePane(double[] row, double[] col) {
        supportDrill = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Close")});
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Drill"), supportDrill);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Widget_Style"), panel);
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
