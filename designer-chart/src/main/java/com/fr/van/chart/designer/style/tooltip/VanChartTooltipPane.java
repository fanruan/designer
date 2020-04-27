package com.fr.van.chart.designer.style.tooltip;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.van.chart.designer.AbstractVanChartScrollPane;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class VanChartTooltipPane extends AbstractVanChartScrollPane<Chart> {
    private static final long serialVersionUID = -2974722365840564105L;
    private VanChartPlotTooltipPane tooltipPane;
    private Chart chart;
    protected VanChartStylePane parent;


    public VanChartTooltipPane(VanChartStylePane parent){
        super();
        this.parent = parent;
    }

    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if(chart == null) {
            return contentPane;
        }
        tooltipPane = getTooltipPane(chart.getPlot());
        contentPane.add(tooltipPane, BorderLayout.NORTH);
        return contentPane;
    }

    @Override
    public void populateBean(Chart chart) {
        this.chart = chart;

        if(tooltipPane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }

        Plot plot = this.chart.getPlot();
        DataSeriesCondition attr = ((VanChartPlot)plot).getAttrTooltipFromConditionCollection();
        if(tooltipPane != null) {
            tooltipPane.populate((AttrTooltip)attr);
        }
    }

    public void updateBean(Chart chart) {
        if(chart == null) {
            return;
        }
        AttrTooltip attrTooltip = tooltipPane.update();
        DataSeriesCondition attr = ((VanChartPlot)chart.getPlot()).getAttrTooltipFromConditionCollection();
        ConditionAttr attrList = chart.getPlot().getConditionCollection().getDefaultAttr();
        if(attr != null) {
            attrList.remove(attr);
        }
        if (attrTooltip != null) {
            attrList.addDataSeriesCondition(attrTooltip);
        }
        //提示埋点
        ChartInfoCollector.getInstance().updateChartConfig(chart, ConfigType.TOOLTIP, chart.getPlot().getBuryingPointTooltipConfig());
    }

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Tooltip");
    }

    protected VanChartPlotTooltipPane getTooltipPane(Plot plot) {
        return PlotFactory.createPlotTooltipPane(plot, parent);
    }

}