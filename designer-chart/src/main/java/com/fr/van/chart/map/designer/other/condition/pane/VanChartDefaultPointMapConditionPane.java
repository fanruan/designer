package com.fr.van.chart.map.designer.other.condition.pane;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.van.chart.designer.other.condition.item.VanChartEffectConditionPane;

/**
 * Created by Mitisky on 16/5/23.
 */
public class VanChartDefaultPointMapConditionPane extends VanChartMapConditionPane {
    public VanChartDefaultPointMapConditionPane(Plot plot) {
        super(plot);
    }

    @Override
    protected void addDiffAction() {
        if(addLabelOrEffectAction()) {
            addLabelAction();
            classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getPointMapPlotDefaultEffect()));
        }
    }
}
