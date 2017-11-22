package com.fr.plugin.chart.heatmap.designer.other;

import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.other.condition.item.VanChartLabelConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartTooltipConditionPane;
import com.fr.plugin.chart.heatmap.VanChartHeatMapPlot;
import com.fr.plugin.chart.map.designer.other.condition.VanChartMapConditionSelectionPane;

import java.awt.*;

/**
 * Created by Mitisky on 16/10/20.
 */
public class VanChartHeatMapConditionPane extends DataSeriesConditionPane {

    public VanChartHeatMapConditionPane(Plot plot) {
        super(plot);
    }

    protected void initComponents() {
        super.initComponents();
        //添加全部条件属性后被遮挡
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    @Override
    protected ChartConditionPane createListConditionPane() {
        return new VanChartMapConditionSelectionPane();
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, plot));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, plot));
    }

    protected void addStyleAction() {
    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartHeatMapPlot.class;
    }
}
