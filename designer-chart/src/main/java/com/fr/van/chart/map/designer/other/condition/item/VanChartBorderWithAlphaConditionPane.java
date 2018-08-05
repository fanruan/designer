package com.fr.van.chart.map.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;

import com.fr.plugin.chart.base.AttrBorderWithAlpha;
import com.fr.van.chart.designer.component.border.VanChartBorderWithAlphaPane;
import com.fr.van.chart.designer.other.condition.item.AbstractNormalMultiLineConditionPane;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/5/23.
 */
public class VanChartBorderWithAlphaConditionPane extends AbstractNormalMultiLineConditionPane {
    private VanChartBorderWithAlphaPane borderWithAlphaPane;

    public VanChartBorderWithAlphaConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    @Override
    protected String getItemLabelString() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Border");
    }

    @Override
    protected JPanel initContentPane() {
        borderWithAlphaPane = new VanChartBorderWithAlphaPane();
        return borderWithAlphaPane;
    }

    /**
     * 条目名称
     *
     * @return 名称
     */
    @Override
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Border");
    }

    @Override
    public void populate(DataSeriesCondition condition) {
        if(condition instanceof AttrBorderWithAlpha){
            borderWithAlphaPane.populate((AttrBorderWithAlpha)condition);
        }

    }

    @Override
    public DataSeriesCondition update() {
        return borderWithAlphaPane.update();
    }

    @Override
    public void setDefault() {
        borderWithAlphaPane.populate(new AttrBorderWithAlpha());
    }
}
