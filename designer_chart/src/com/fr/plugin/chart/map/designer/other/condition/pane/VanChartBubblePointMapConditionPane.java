package com.fr.plugin.chart.map.designer.other.condition.pane;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.bubble.attr.VanChartAttrBubble;
import com.fr.plugin.chart.designer.other.condition.item.VanChartEffectConditionPane;
import com.fr.plugin.chart.map.designer.other.condition.item.VanChartBubbleMarkerConditionPane;

/**
 * Created by Mitisky on 16/5/23.
 * 标记点为气泡标记点设置
 */
public class VanChartBubblePointMapConditionPane extends VanChartMapConditionPane {
    public VanChartBubblePointMapConditionPane(Plot plot) {
        super(plot);
    }

    @Override
    protected void addDiffAction() {
        classPaneMap.put(VanChartAttrBubble.class, new VanChartBubbleMarkerConditionPane(this));
        if (addLabelOrEffectAction()) {
            addLabelAction();
            classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getBubblePlotDefaultEffect()));
        }
    }
}
