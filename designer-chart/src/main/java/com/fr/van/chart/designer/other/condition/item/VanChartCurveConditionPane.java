package com.fr.van.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;

import com.fr.plugin.chart.map.line.condition.AttrCurve;
import com.fr.van.chart.map.line.VanChartCurvePane;

import javax.swing.JPanel;

/**
 * Created by hufan on 2016/12/23.
 */
public class VanChartCurveConditionPane extends AbstractNormalMultiLineConditionPane{
    private VanChartCurvePane curvePane;

    public VanChartCurveConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    @Override
    protected String getItemLabelString() {
        return nameForPopupMenuItem();
    }

    @Override
    protected JPanel initContentPane() {
        this.curvePane = new VanChartCurvePane();
        return this.curvePane;
    }

    public void setDefault() {
        //下面这句话是给各组件一个默认值
        AttrCurve attrCurve = new AttrCurve();
        curvePane.populateBean(attrCurve);
    }

    @Override
    public void populate(DataSeriesCondition condition) {
        if(condition instanceof AttrCurve){
            curvePane.populateBean((AttrCurve) condition);
        }

    }

    @Override
    public DataSeriesCondition update() {
        return curvePane.updateBean();
    }
    @Override
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Curve");
    }

}
