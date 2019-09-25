package com.fr.van.chart.line;

import com.fr.chart.chartattr.Plot;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.van.chart.designer.style.series.VanChartAbstractPlotSeriesPane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * 折线图的系列界面
 */
public class VanChartLineSeriesPane extends VanChartAbstractPlotSeriesPane {
    private static final long serialVersionUID = 5595016643808487932L;

    public VanChartLineSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p, p, p, p, p, p, p, p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{createLineTypePane()},
                new Component[]{createMarkerPane()},
                new Component[]{createStackedAndAxisPane()},
                new Component[]{createTrendLinePane()},
        };

        contentPane = TableLayoutHelper.createTableLayoutPane(components, row, col);
        return contentPane;
    }

}