package com.fr.van.chart.gantt.designer.other;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAlphaPane;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.gantt.VanChartGanttDataPoint;
import com.fr.plugin.chart.gantt.VanChartGanttPlot;
import com.fr.plugin.chart.gantt.attr.AttrGanttLabel;
import com.fr.plugin.chart.gantt.attr.AttrGanttTooltip;
import com.fr.plugin.chart.gantt.attr.AttrGanttTooltipContent;
import com.fr.van.chart.designer.other.condition.item.VanChartLabelConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartSeriesColorConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartTooltipConditionPane;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotTooltipNoCheckPane;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotTooltipPane;

import java.awt.Dimension;

/**
 * Created by hufan on 2017/1/13.
 */
public class VanChartGanttConditionPane extends DataSeriesConditionPane {
    private static final long serialVersionUID = -7180705321732069806L;

    public VanChartGanttConditionPane(Plot plot) {
        super(plot);
    }

    protected void initComponents() {
        super.initComponents();
        //添加全部条件属性后被遮挡
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(AttrAlpha.class, new LabelAlphaPane(this));
        classPaneMap.put(AttrGanttLabel.class, new VanChartLabelConditionPane(this, plot));
        classPaneMap.put(AttrGanttTooltip.class, new VanChartTooltipConditionPane(this, plot){

            @Override
            protected VanChartPlotTooltipPane createTooltipContentsPane() {
                return new VanChartPlotTooltipNoCheckPane(getPlot(), null){
                    @Override
                    protected AttrTooltip getAttrTooltip() {
                        AttrGanttTooltip attrGanttTooltip = new AttrGanttTooltip();
                        ((AttrGanttTooltipContent)attrGanttTooltip.getContent()).getDurationFormat().setEnable(true);
                        return attrGanttTooltip;
                    }
                };

            }
        });

    }

    protected void addStyleAction() {

    }

    @Override
    protected ChartConditionPane createListConditionPane() {
        return new ChartConditionPane(){
            @Override
            public String[] columns2Populate() {
                return new String[]{
                        VanChartGanttDataPoint.PROJECT_NAME,
                        VanChartGanttDataPoint.PROJECT_INDEX,
                        ChartConstants.SERIES_NAME,
                        ChartConstants.SERIES_INDEX,
                        VanChartGanttDataPoint.START_TIME,
                        VanChartGanttDataPoint.END_TIME,
                        VanChartGanttDataPoint.PROGRESS,
                        VanChartGanttDataPoint.LINK_ID
                };
            }
        };
    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartGanttPlot.class;
    }
}
