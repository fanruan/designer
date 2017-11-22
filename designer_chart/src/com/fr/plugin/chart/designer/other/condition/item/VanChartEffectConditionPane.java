package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.designer.style.series.VanChartEffectPane;

import javax.swing.*;

/**
 * Created by shine on 2016/12/13.
 */
public class VanChartEffectConditionPane extends AbstractNormalMultiLineConditionPane{
    protected VanChartEffectPane effectPane;
    private AttrEffect defaultAttrEffect;

    @Override
    protected String getItemLabelString() {
        return nameForPopupMenuItem();
    }

    @Override
    protected JPanel initContentPane() {
        effectPane = createEffectPane();
        return effectPane;
    }

    public VanChartEffectConditionPane(ConditionAttributesPane conditionAttributesPane, AttrEffect effect) {
        super(conditionAttributesPane);
        this.defaultAttrEffect = effect;
    }

    protected VanChartEffectPane createEffectPane() {
        return new VanChartEffectPane(false);
    }

    public void setDefault() {
        //下面这句话是给各组件一个默认值
        if(defaultAttrEffect != null) {
            effectPane.populateBean(defaultAttrEffect);
        }
    }

    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_Flash_Animation");
    }

    @Override
    public void populate(DataSeriesCondition condition) {
        if(condition instanceof AttrEffect){
            effectPane.populateBean((AttrEffect) condition);
        }
    }

    @Override
    public DataSeriesCondition update() {
        return effectPane.updateBean();
    }
}
