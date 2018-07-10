package com.fr.van.chart.map.designer.other.condition.pane;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.van.chart.designer.other.condition.item.VanChartEffectConditionPane;
import com.fr.van.chart.map.designer.other.condition.item.VanChartCommonMarkerConditionPane;

/**
 * Created by Mitisky on 16/5/23.
 * 标记点为通用标记点配置
 */
public class VanChartCommonPointMapConditionPane extends VanChartMapConditionPane{
    public VanChartCommonPointMapConditionPane(Plot plot) {
        super(plot);
    }

    @Override
    protected void addDiffAction() {
        classPaneMap.put(VanChartAttrMarker.class, new VanChartCommonMarkerConditionPane(this));
        if(addLabelOrEffectAction()) {
            addLabelAction();
            classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getScatterPlotDefaultEffect()));
        }
    }
}
