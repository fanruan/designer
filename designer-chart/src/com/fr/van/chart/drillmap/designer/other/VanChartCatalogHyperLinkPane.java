package com.fr.van.chart.drillmap.designer.other;

import com.fr.base.BaseFormula;
import com.fr.base.FormulaBuilder;
import com.fr.chart.chartattr.Plot;
import com.fr.extended.chart.HyperLinkPara;
import com.fr.extended.chart.HyperLinkParaHelper;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.van.chart.custom.component.VanChartHyperLinkPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hufan on 2016/12/13.
 */
public class VanChartCatalogHyperLinkPane extends VanChartHyperLinkPane {

    protected Map<String, BaseFormula> getHyperLinkEditorMap() {
        FormulaBuilder builder = BaseFormula.createFormulaBuilder();
        HashMap<String, BaseFormula> map = new HashMap<String, BaseFormula>();
        for (HyperLinkPara para : HyperLinkParaHelper.DRILL_TOOLS) {
            map.put(para.getName(), builder.build(para.getFormulaContent()));
        }
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
