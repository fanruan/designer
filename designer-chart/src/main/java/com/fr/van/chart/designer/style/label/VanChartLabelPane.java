package com.fr.van.chart.designer.style.label;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.van.chart.designer.AbstractVanChartScrollPane;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class VanChartLabelPane extends AbstractVanChartScrollPane<Chart> {
    private VanChartPlotLabelPane labelPane;
    private Chart chart;
    protected VanChartStylePane parent;
    private static final long serialVersionUID = -5449293740965811991L;


    public VanChartLabelPane(VanChartStylePane parent){
        super();
        this.parent = parent;
    }

    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if(chart == null) {
            return contentPane;
        }
        labelPane = getLabelPane(chart.getPlot());
        contentPane.add(labelPane, BorderLayout.NORTH);
        return contentPane;
    }

    @Override
    public void populateBean(Chart chart) {
        this.chart = chart;

        if(labelPane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }

        Plot plot = this.chart.getPlot();

        DataSeriesCondition attr = ((VanChartPlot)plot).getAttrLabelFromConditionCollection();

        if(labelPane != null) {
            labelPane.populate((AttrLabel)attr);
        }
    }


    public void updateBean(Chart chart) {
        if(chart == null) {
            return;
        }
        ConditionAttr attrList = chart.getPlot().getConditionCollection().getDefaultAttr();
        DataSeriesCondition attr = ((VanChartPlot)chart.getPlot()).getAttrLabelFromConditionCollection();
        if(attr != null) {
            attrList.remove(attr);
        }
        AttrLabel attrLabel = labelPane.update();
        if (attrLabel != null) {
            attrList.addDataSeriesCondition(attrLabel);
        }
        //标签埋点
        ChartInfoCollector.getInstance().updateChartConfig(chart, ConfigType.LABEL, chart.getBuryingPointLabelConfig());
    }

    @Override
    protected String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_LABEL_TITLE;
    }

    protected VanChartPlotLabelPane getLabelPane(Plot plot) {
        return PlotFactory.createPlotLabelPane(plot, parent);
    }

}