package com.fr.van.chart.structure.desinger.style;


import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.multilayer.style.AttrTooltipMultiLevelNameFormat;
import com.fr.van.chart.designer.component.VanChartLabelContentPane;
import com.fr.van.chart.designer.component.format.CategoryNameFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.PercentFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.SeriesNameFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.ValueFormatPaneWithCheckBox;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by shine on 2017/2/15.
 */
public class VanChartStructureLabelContentPane extends VanChartLabelContentPane {
    public VanChartStructureLabelContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{categoryNameFormatPane,null},
                new Component[]{seriesNameFormatPane,null},
                new Component[]{valueFormatPane,null}
        };
    }

    @Override
    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane){
        categoryNameFormatPane = new CategoryNameFormatPaneWithCheckBox(parent, showOnPane){
            @Override
            protected String getCheckBoxText() {
                return com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Node_Name");
            }
        };
        seriesNameFormatPane = new SeriesNameFormatPaneWithCheckBox(parent, showOnPane){
            @Override
            protected String getCheckBoxText() {
                return com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_MultiPie_Series_Name");
            }
        };
        valueFormatPane = new ValueFormatPaneWithCheckBox(parent, showOnPane);
        percentFormatPane = new PercentFormatPaneWithCheckBox(parent, showOnPane);
    }

    @Override
    protected AttrTooltipContent createAttrTooltip() {
        AttrTooltipContent attrTooltipContent = new AttrTooltipContent();
        attrTooltipContent.setCategoryFormat(new AttrTooltipMultiLevelNameFormat());
        return attrTooltipContent;
    }
}
