package com.fr.plugin.chart.designer.style.tooltip;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import java.awt.*;

/**
 * Created by Mitisky on 15/8/29.
 */
public class VanChartPlotTooltipNoCheckPane extends VanChartPlotTooltipPane {

    public VanChartPlotTooltipNoCheckPane(Plot plot,VanChartStylePane parent) {
        super(plot, parent);
    }

    protected  void addComponents(Plot plot) {
        isTooltipShow = new UICheckBox(Inter.getLocText("Plugin-ChartF_UseTooltip"));
        tooltipPane = createTooltipPane(plot);

        this.setLayout(new BorderLayout());
        this.add(tooltipPane,BorderLayout.CENTER);
    }


    public void populate(AttrTooltip attr) {
        super.populate(attr);
        isTooltipShow.setSelected(true);
        tooltipPane.setEnabled(isTooltipShow.isSelected());
    }

}