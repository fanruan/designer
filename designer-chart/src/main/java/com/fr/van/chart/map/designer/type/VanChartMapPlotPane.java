package com.fr.van.chart.map.designer.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.chartx.data.MapChartDataDefinition;
import com.fr.design.mainframe.chart.gui.type.ChartImagePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.map.MapIndependentVanChart;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.data.VanMapDefinition;
import com.fr.plugin.chart.map.server.CompatibleGEOJSONHelper;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by Mitisky on 16/5/4.
 */
public class VanChartMapPlotPane extends AbstractVanChartTypePane {

    private VanChartMapSourceChoosePane sourceChoosePane;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/map/images/area-map.png",
                "/com/fr/van/chart/map/images/point-map.png",
                "/com/fr/van/chart/map/images/line-map.png",
                "/com/fr/van/chart/map/images/custom-map.png"
        };
    }

    protected Component[][] getPaneComponents(JPanel typePane) {
        try {
            sourceChoosePane = createSourceChoosePane();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return new Component[][]{
                new Component[]{typePane},
                new Component[]{sourceChoosePane}
        };
    }

    protected VanChartMapSourceChoosePane createSourceChoosePane() {
        return new VanChartMapSourceChoosePane();
    }

    /**
     * 更新界面内容
     */
    public void populateBean(Chart chart) {
        for (ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        VanChartMapPlot plot = (VanChartMapPlot) chart.getPlot();

        typeDemo.get(plot.getDetailType()).isPressing = true;
        populateSourcePane(plot);

        boolean enabled = !CompatibleGEOJSONHelper.isDeprecated(plot.getGeoUrl());
        GUICoreUtils.setEnabled(this.getTypePane(), enabled);
        GUICoreUtils.setEnabled(this.sourceChoosePane.getSourceComboBox(), enabled);

        checkDemosBackground();
    }

    protected void populateSourcePane(VanChartMapPlot plot) {
        //populate需要使用clone的plot
        try {
            VanChartMapPlot mapPlot = (VanChartMapPlot) plot.clone();
            sourceChoosePane.populateBean(mapPlot);
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    public void updateBean(Chart chart) {
        super.updateBean(chart);
        Plot plot = chart.getPlot();
        if (plot instanceof VanChartMapPlot) {
            sourceChoosePane.updateBean((VanChartMapPlot) plot);
            if (!isSamePlot() || (typeChanged && isSamePlot())) {
                resetAttr(plot);
            }
        }
    }
    /**
     * 不同地图类型的超链不需要复制
     *
     * @param oldPlot
     * @param newPlot
     * @throws CloneNotSupportedException
     */
    protected void cloneHotHyperLink(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException {

    }

    @Override
    protected void resetFilterDefinition(Chart chart) {
        chart.setFilterDefinition(new VanMapDefinition());
    }

    protected void resetAttr(Plot plot) {
        sourceChoosePane.resetComponentValue((VanChartMapPlot) plot);
    }

    protected Plot getSelectedClonedPlot() {
        VanChartMapPlot newPlot = null;
        Chart[] charts = getDefaultCharts();
        for (int i = 0, len = charts.length; i < len; i++) {
            if (typeDemo.get(i).isPressing) {
                newPlot = (VanChartMapPlot) charts[i].getPlot();
            }
        }
        Plot cloned = null;
        if (null == newPlot) {
            return cloned;
        }
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return cloned;
    }

    @Override
    protected VanChartTools createVanChartTools() {
        VanChartTools tools = new VanChartTools();
        tools.setSort(false);
        tools.setExport(false);
        return tools;
    }

    protected Chart[] getDefaultCharts() {
        return MapIndependentVanChart.MapVanCharts;
    }

    public Chart getDefaultChart() {
        return MapIndependentVanChart.MapVanCharts[0];
    }

    public VanChartMapSourceChoosePane getSourceChoosePane() {
        return this.sourceChoosePane;
    }

    @Override
    protected boolean acceptDefinition(ChartDataDefinitionProvider definition, VanChartPlot vanChartPlot) {
        return definition instanceof MapChartDataDefinition;
    }
}
