package com.fr.van.chart.designer.other.condition.item;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Plot;
import com.fr.design.condition.ConditionAttributesPane;

import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.van.chart.designer.component.VanChartAreaSeriesFillColorPane;

import javax.swing.JPanel;

/**
 * 面积图，填充色
 */
public class VanChartAreaFillColorConditionPane  extends AbstractNormalMultiLineConditionPane {
    private Plot plot;
    private static final long serialVersionUID = -4148284851967140012L;
    private VanChartAreaSeriesFillColorPane fillColorBackground;

    @Override
    protected String getItemLabelString() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Fill_Color");
    }

    @Override
    protected JPanel initContentPane() {
        fillColorBackground = new VanChartAreaSeriesFillColorPane();
        return fillColorBackground;
    }

    public VanChartAreaFillColorConditionPane(ConditionAttributesPane conditionAttributesPane, Plot plot) {
        super(conditionAttributesPane);
        this.plot = plot;
    }

    public VanChartAreaFillColorConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Fill_Color");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void setDefault() {
        //下面这句话是给各组件一个默认值
        fillColorBackground.populate(new AttrAreaSeriesFillColorBackground());
        fillColorBackground.checkoutAlpha(!(plot != null && plot.getPlotStyle() == ChartConstants.STYLE_SHADE));
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrAreaSeriesFillColorBackground) {
            fillColorBackground.populate((AttrAreaSeriesFillColorBackground) condition);
            fillColorBackground.checkoutAlpha(!(plot != null &&  plot.getPlotStyle() == ChartConstants.STYLE_SHADE));
        }
    }

    public DataSeriesCondition update() {
        return fillColorBackground.update();
    }
}