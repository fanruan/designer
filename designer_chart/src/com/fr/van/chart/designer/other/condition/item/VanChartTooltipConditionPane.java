package com.fr.van.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Plot;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotTooltipNoCheckPane;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotTooltipPane;

import javax.swing.JPanel;

public class VanChartTooltipConditionPane extends AbstractNormalMultiLineConditionPane {

    private static final long serialVersionUID = 7514028150764584873L;
    private VanChartPlotTooltipPane tooltipContentsPane;

    @Override
    protected String getItemLabelString() {
        return nameForPopupMenuItem();
    }

    @Override
    protected JPanel initContentPane() {
        this.tooltipContentsPane = createTooltipContentsPane();
        return this.tooltipContentsPane;
    }

    public VanChartTooltipConditionPane(ConditionAttributesPane conditionAttributesPane, Plot plot) {
        super(conditionAttributesPane, plot);
    }

    protected VanChartPlotTooltipPane createTooltipContentsPane() {
        return new VanChartPlotTooltipNoCheckPane(getPlot(), null);
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_Tooltip");
    }

    public void setDefault() {
        //下面这句话是给各个组件一个默认值
        AttrTooltip tooltip = getPlot() == null ? new AttrTooltip() : getPlot().getDefaultAttrTooltip();

        tooltip.setShowMutiSeries(getPlot() != null && getPlot().isInCustom());

        this.tooltipContentsPane.populate(tooltip);
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrTooltip) {
            this.tooltipContentsPane.populate((AttrTooltip) condition);
        }
    }

    public DataSeriesCondition update() {
        return this.tooltipContentsPane.update();
    }
}