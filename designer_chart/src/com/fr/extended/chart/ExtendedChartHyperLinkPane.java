package com.fr.extended.chart;

import com.fr.base.BaseFormula;
import com.fr.chart.chartattr.Plot;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.chart.custom.component.VanChartHyperLinkPane;

import java.util.Map;

/**
 * Created by shine on 2018/3/13.
 */
public class ExtendedChartHyperLinkPane extends VanChartHyperLinkPane {
    private AbstractChart chart;

    public void populateBean(AbstractChart chart) {
        this.chart = chart;
        populate(chart.getPlot());
    }

    public void updateBean(AbstractChart chart) {
        update(chart.getPlot());
    }

    @Override
    protected Map<String, BaseFormula> getHyperLinkEditorMap() {
        return chart.getHyperLinkEditorMap();
    }

    @Override
    protected NameJavaScriptGroup populateHotHyperLink(Plot plot) {
        return chart.getLinkGroup();
    }

    @Override
    protected void updateHotHyperLink(Plot plot, NameJavaScriptGroup nameGroup) {
        chart.setLinkGroup(nameGroup);
    }
}
