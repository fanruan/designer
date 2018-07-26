package com.fr.van.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;

import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.van.chart.designer.component.VanChartTrendLinePane;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 15/10/19.
 */
public class VanChartTrendLineConditionPane extends AbstractNormalMultiLineConditionPane{
    private VanChartTrendLinePane trendLinePane;

    @Override
    protected String getItemLabelString() {
        return com.fr.design.i18n.Toolkit.i18nText("Chart-Trend_Line");
    }

    @Override
    protected JPanel initContentPane() {
        trendLinePane = new VanChartTrendLinePane();
        return trendLinePane;
    }

    public VanChartTrendLineConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Chart-Trend_Line");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof VanChartAttrTrendLine) {
            this.trendLinePane.populate((VanChartAttrTrendLine) condition);
        }
    }

    public DataSeriesCondition update() {
        return this.trendLinePane.update();
    }



}