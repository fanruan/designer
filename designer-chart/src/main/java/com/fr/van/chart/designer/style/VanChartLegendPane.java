package com.fr.van.chart.designer.style;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.plugin.chart.attr.VanChartLegend;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.AbstractVanChartScrollPane;
import com.fr.van.chart.designer.PlotFactory;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * 属性表, 图表样式-图例 界面.
 */
public class VanChartLegendPane extends AbstractVanChartScrollPane<VanChart> {
    private static final long serialVersionUID = 7553135492053931171L;

    private VanChartPlotLegendPane legendContent;
    protected VanChartStylePane parent;
    protected Chart chart;

    public VanChartLegendPane(VanChartStylePane parent) {
        super();
        this.parent = parent;
    }


    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_LEGNED_TITLE;
    }

    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if (chart == null) {
            return contentPane;
        }
        legendContent = getPlotLegendPane(chart.getPlot());
        if (legendContent != null) {
            contentPane.add(legendContent, BorderLayout.CENTER);
        }
        return contentPane;
    }

    @Override
    public void updateBean(VanChart chart) {
        Plot plot = this.chart.getPlot();
        if (plot == null) {
            return;
        }
        VanChartLegend legend = (VanChartLegend) plot.getLegend();
        legendContent.updateBean(legend);
    }

    @Override
    public void populateBean(VanChart chart) {

        this.chart = chart;
        if (legendContent == null) {
            this.remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }

        Plot plot = this.chart.getPlot();
        if (plot == null) {
            return;
        }
        legendContent.setPlot(plot);
        VanChartLegend legend = (VanChartLegend) plot.getLegend();
        if (legendContent != null) {
            legendContent.populateBean(legend);
        }
    }

    /**
     * 初始化不同的Plot图例界面.
     */
    protected VanChartPlotLegendPane getPlotLegendPane(Plot plot) {
        return PlotFactory.createPlotLegendPane(plot, parent);
    }
}