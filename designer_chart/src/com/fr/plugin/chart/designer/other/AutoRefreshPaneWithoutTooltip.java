package com.fr.plugin.chart.designer.other;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.RefreshMoreLabel;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mengao on 2017/6/21.
 */
public class AutoRefreshPaneWithoutTooltip extends AutoRefreshPane {

    public AutoRefreshPaneWithoutTooltip(VanChart chart, boolean isLargeModel) {
        super(chart, isLargeModel);
    }

    protected Component[][] initComponent(JPanel autoTooltipPane){
        return new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Time_Interval")), getAutoRefreshTime(),new UILabel(Inter.getLocText("Chart-Time_Seconds"))}
        };
    }

    protected void updateAutoTooltip(RefreshMoreLabel refreshMoreLabel) {
        refreshMoreLabel.setAutoTooltip(false);
    }

}
