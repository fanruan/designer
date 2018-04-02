package com.fr.van.chart.designer.style.background;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.general.Inter;
import com.fr.van.chart.designer.AbstractVanChartScrollPane;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.background.VanChartBackgroundPane;
import com.fr.van.chart.designer.component.border.VanChartBorderWithRadiusPane;
import com.fr.van.chart.designer.style.background.radar.VanChartRadarAxisAreaPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

//图表区|绘图区 边框和背景
public class VanChartAreaBackgroundPane extends AbstractVanChartScrollPane<Chart> {

    private static final long serialVersionUID = 104381641640066015L;
    protected VanChartBorderWithRadiusPane chartBorderPane;
    protected VanChartBackgroundPane chartBackgroundPane;
    private VanChartAxisAreaPane chartAxisAreaPane;

    private JPanel contentPane;

    private boolean isPlot;//绘图区

    private AbstractAttrNoScrollPane parent;

    public VanChartAreaBackgroundPane(boolean isPlot, AbstractAttrNoScrollPane parent){
        super();
        this.isPlot = isPlot;
        this.parent = parent;
    }

    @Override
    protected JPanel createContentPane() {
        contentPane = new JPanel(new BorderLayout());
        chartBorderPane = new VanChartBorderWithRadiusPane();
        chartBackgroundPane = new VanChartBackgroundPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { f };
        double[] rowSize = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Border"),chartBorderPane)},
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Background"), chartBackgroundPane)},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        contentPane.add(panel, BorderLayout.CENTER);
        return contentPane;
    }

    private void refreshContentPane(Plot plot) {
        contentPane.removeAll();
        chartBorderPane = new VanChartBorderWithRadiusPane();
        chartBackgroundPane = new VanChartBackgroundPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { f };
        double[] rowSize = {p,p,p};
        Component[][] components;

        if(plot.isSupportBorder()){//有边框和背景

            chartAxisAreaPane = initAxisAreaPane();

            components = initComponents();
        } else {
            chartAxisAreaPane = new VanChartRadarAxisAreaPane();
            components = new Component[][]{
                    new Component[]{chartAxisAreaPane},
            };
        }

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        contentPane.add(panel,BorderLayout.CENTER);

        parent.initAllListeners();
    }

    protected VanChartAxisAreaPane initAxisAreaPane() {
        return new VanChartAxisAreaPane();
    }

    protected Component[][] initComponents() {
        return new Component[][]{
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Border"),chartBorderPane)},
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Background"), chartBackgroundPane)},
                new Component[]{chartAxisAreaPane}
        };
    }

    /**
     *       标题
     *    @return 标题
     */
    public String title4PopupWindow() {
        if(isPlot){
            return PaneTitleConstants.CHART_STYLE_AREA_PLOT_TITLE;
        }
        return PaneTitleConstants.CHART_STYLE_AREA_AREA_TITLE;
    }

    @Override
    public void updateBean(Chart chart) {
        if (chart == null) {
            chart = new Chart();
        }
        if(isPlot){
            Plot plot = chart.getPlot();
            chartBorderPane.update(plot);
            chartBackgroundPane.update(plot);
            if(plot.isSupportIntervalBackground() && chartAxisAreaPane != null){
                chartAxisAreaPane.updateBean(plot);
            }
        } else {
            chartBorderPane.update(chart);
            chartBackgroundPane.update(chart);
        }
    }

    @Override
    public void populateBean(Chart chart) {
        if(chart == null) {
            return;
        }
        if(isPlot){
            Plot plot = chart.getPlot();
            if(plot.isSupportIntervalBackground()){
                //含有坐标轴相关设置，例如警戒线、网格线、间隔背景
                if (chartAxisAreaPane == null) {
                    refreshContentPane(plot);
                }
                chartAxisAreaPane.populateBean(plot);
            }
            chartBorderPane.populate(plot);
            chartBackgroundPane.populate(plot);
        } else {
            chartBorderPane.populate(chart);
            chartBackgroundPane.populate(chart);
        }
    }

    @Override
    public Chart updateBean() {
        return null;
    }
}