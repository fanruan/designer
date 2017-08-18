package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.designer.style.series.VanChartEffectPane;
import com.fr.plugin.chart.map.line.VanChartLineMapEffectPane;
import com.fr.plugin.chart.map.line.condition.AttrLineEffect;

/**
 * Created by hufan on 2016/12/23.
 */
public class VanChartLineEffectConditionPane extends VanChartEffectConditionPane {

    public VanChartLineEffectConditionPane(ConditionAttributesPane conditionAttributesPane, AttrEffect effect) {
        super(conditionAttributesPane, effect);
    }

    @Override
    protected VanChartEffectPane createEffectPane() {
        return new VanChartLineMapEffectPane();
    }

    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_Line_Map_Animation");
    }

    @Override
    public void populate(DataSeriesCondition condition) {
        if(condition instanceof AttrLineEffect){
            effectPane.populateBean((AttrLineEffect) condition);
        }
    }

}
