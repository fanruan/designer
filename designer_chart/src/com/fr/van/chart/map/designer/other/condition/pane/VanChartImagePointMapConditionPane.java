package com.fr.van.chart.map.designer.other.condition.pane;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.van.chart.map.designer.other.condition.item.VanChartImageMarkerConditionPane;

/**
 * Created by Mitisky on 16/5/23.
 * 标记点为自定义图片
 */
public class VanChartImagePointMapConditionPane extends VanChartMapConditionPane {
    public VanChartImagePointMapConditionPane(Plot plot) {
        super(plot);
    }

    @Override
    protected void addDiffAction() {
        classPaneMap.put(VanChartAttrMarker.class, new VanChartImageMarkerConditionPane(this));
        if (addLabelOrEffectAction()) {
            addLabelAction();
        }
    }
}
