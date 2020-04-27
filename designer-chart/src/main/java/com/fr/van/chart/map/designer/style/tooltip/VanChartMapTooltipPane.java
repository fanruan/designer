package com.fr.van.chart.map.designer.style.tooltip;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.attr.AttrMapTooltip;
import com.fr.van.chart.designer.AbstractVanChartScrollPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotTooltipPane;
import com.fr.van.chart.map.designer.VanMapAreaAndPointGroupPane;
import com.fr.van.chart.map.designer.VanMapAreaPointAndLineGroupPane;
import com.fr.van.chart.map.line.VanChartLineMapPlotTooltipPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by Mitisky on 16/5/20.
 */
public class VanChartMapTooltipPane extends AbstractVanChartScrollPane<Chart> {
    private VanChartPlotTooltipPane areaTooltipPane;
    private VanChartPlotTooltipPane pointTooltipPane;
    private VanChartPlotTooltipPane lineTooltipPane;

    private VanChartMapPlot mapPlot;
    protected VanChartStylePane parent;
    private static final long serialVersionUID = -5449293740965811991L;

    public VanChartMapTooltipPane(VanChartStylePane parent) {
        super();
        this.parent = parent;
    }

    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if (mapPlot == null) {
            return contentPane;
        }
        switch (mapPlot.getAllLayersMapType()){
            case POINT:
                pointTooltipPane = new VanChartPlotTooltipPane(mapPlot, parent);
                contentPane.add(pointTooltipPane, BorderLayout.NORTH);
                break;
            case LINE:
                lineTooltipPane = new VanChartLineMapPlotTooltipPane(mapPlot, parent);
                contentPane.add(lineTooltipPane, BorderLayout.NORTH);
                break;
            case CUSTOM:
                areaTooltipPane = new VanChartPlotTooltipPane(mapPlot, parent);
                pointTooltipPane = new VanChartPlotTooltipPane(mapPlot, parent);
                lineTooltipPane = new VanChartLineMapPlotTooltipPane(mapPlot, parent);
                contentPane.add(new VanMapAreaPointAndLineGroupPane(areaTooltipPane, pointTooltipPane, lineTooltipPane), BorderLayout.NORTH);
                break;
            case DRILL_CUSTOM:
                areaTooltipPane = new VanChartPlotTooltipPane(mapPlot, parent);
                pointTooltipPane = new VanChartPlotTooltipPane(mapPlot, parent);
                contentPane.add(new VanMapAreaAndPointGroupPane(areaTooltipPane, pointTooltipPane), BorderLayout.NORTH);
                break;
            default:
                areaTooltipPane = new VanChartPlotTooltipPane(mapPlot, parent);
                contentPane.add(areaTooltipPane, BorderLayout.NORTH);
                break;
        }

        return contentPane;
    }

    @Override
    public void populateBean(Chart chart) {
        Plot plot = chart.getPlot();
        if(plot instanceof VanChartMapPlot){
            mapPlot = (VanChartMapPlot) plot;
        }

        if (areaTooltipPane == null && pointTooltipPane == null && lineTooltipPane == null) {
            this.remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }

        AttrMapTooltip attrMapTooltip = (AttrMapTooltip)plot.getConditionCollection().getDefaultAttr().getExisted(AttrMapTooltip.class);
        if(attrMapTooltip == null){
            attrMapTooltip = new AttrMapTooltip();
        }
        if(pointTooltipPane != null){
            pointTooltipPane.populate(attrMapTooltip.getPointTooltip());
        }
        if (areaTooltipPane != null) {
            areaTooltipPane.populate(attrMapTooltip.getAreaTooltip());
        }
        if (lineTooltipPane != null){
            lineTooltipPane.populate(attrMapTooltip.getLineTooltip());
        }
    }


    public void updateBean(Chart chart) {
        if (chart == null) {
            return;
        }
        ConditionAttr defaultAttr = chart.getPlot().getConditionCollection().getDefaultAttr();
        AttrMapTooltip attrMapTooltip = (AttrMapTooltip)defaultAttr.getExisted(AttrMapTooltip.class);
        if (attrMapTooltip != null) {
            defaultAttr.remove(attrMapTooltip);
        } else {
            attrMapTooltip = new AttrMapTooltip();
        }

        if(areaTooltipPane != null){
            attrMapTooltip.setAreaTooltip(areaTooltipPane.update());
        }
        if(pointTooltipPane != null){
            attrMapTooltip.setPointTooltip(pointTooltipPane.update());
        }
        if (lineTooltipPane != null){
            attrMapTooltip.setLineTooltip(lineTooltipPane.update());
        }
        defaultAttr.addDataSeriesCondition(attrMapTooltip);
        //提示埋点
        ChartInfoCollector.getInstance().updateChartConfig(chart, ConfigType.TOOLTIP, chart.getPlot().getBuryingPointTooltipConfig());
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Tooltip");
    }
}

