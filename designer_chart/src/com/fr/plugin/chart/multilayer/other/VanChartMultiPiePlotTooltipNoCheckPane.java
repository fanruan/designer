package com.fr.plugin.chart.multilayer.other;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.multilayer.style.VanChartMultiPiePlotTooltipPane;

import java.awt.*;

/**
 * Created by Fangjie on 2016/7/4.
 */
public class VanChartMultiPiePlotTooltipNoCheckPane extends VanChartMultiPiePlotTooltipPane {
    public VanChartMultiPiePlotTooltipNoCheckPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    protected  void addComponents(Plot plot) {
        isTooltipShow = new UICheckBox(Inter.getLocText("Plugin-ChartF_UseTooltip"));
        tooltipPane = createTooltipPane(plot);

        this.setLayout(new BorderLayout());
        this.add(tooltipPane,BorderLayout.CENTER);
    }

    @Override
    public void populate(AttrTooltip attr) {
        super.populate(attr);
        isTooltipShow.setSelected(true);
        tooltipPane.setEnabled(isTooltipShow.isSelected());
    }
}
