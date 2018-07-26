package com.fr.van.chart.multilayer.other;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.icheckbox.UICheckBox;

import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.multilayer.style.VanChartMultiPiePlotTooltipPane;

import java.awt.BorderLayout;

/**
 * Created by Fangjie on 2016/7/4.
 */
public class VanChartMultiPiePlotTooltipNoCheckPane extends VanChartMultiPiePlotTooltipPane {
    public VanChartMultiPiePlotTooltipNoCheckPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    protected  void addComponents(Plot plot) {
        isTooltipShow = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_UseTooltip"));
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
