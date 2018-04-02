package com.fr.van.chart.designer.other;

import com.fr.plugin.chart.base.RefreshMoreLabel;
import com.fr.plugin.chart.vanchart.VanChart;

import java.awt.Component;

/**
 * Created by mengao on 2017/6/21.
 */
public class AutoRefreshPaneWithoutTooltip extends AutoRefreshPane {

    public AutoRefreshPaneWithoutTooltip(VanChart chart, boolean isLargeModel) {
        super(chart, isLargeModel);
    }

    protected Component[][] initAutoTooltipComponent () {
        return new Component[][]{
                new Component[]{null, null},
        };
    }

    protected void updateAutoTooltip(RefreshMoreLabel refreshMoreLabel) {
        refreshMoreLabel.setAutoTooltip(false);
    }

}
