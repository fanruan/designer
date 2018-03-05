package com.fr.plugin.chart.drillmap.designer.other;

import com.fr.base.BaseFormula;
import com.fr.chart.chartattr.Plot;
import com.fr.general.Inter;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.chart.custom.component.VanChartHyperLinkPane;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hufan on 2016/12/13.
 */
public class VanChartCatalogHyperLinkPane extends VanChartHyperLinkPane{

    @Override
    protected Map<String, BaseFormula> getHyperLinkEditorMap() {
        HashMap<String, BaseFormula> map = new HashMap<String, BaseFormula>();
        map.put(Inter.getLocText("FR-Chart-Area_Name"), BaseFormula.createFormulaBuilder().build("AREA_NAME"));
        return map;
    }

    protected void updateHotHyperLink(Plot plot, NameJavaScriptGroup nameGroup) {
        if (plot instanceof VanChartDrillMapPlot) {
            ((VanChartDrillMapPlot) plot).setDrillUpHyperLink(nameGroup);
        }
    }

    protected NameJavaScriptGroup populateHotHyperLink(Plot plot) {
        if (plot instanceof VanChartDrillMapPlot) {
            return ((VanChartDrillMapPlot) plot).getDrillUpHyperLink();
        }

        return null;
    }
}
