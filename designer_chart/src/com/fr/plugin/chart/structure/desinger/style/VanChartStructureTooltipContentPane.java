package com.fr.plugin.chart.structure.desinger.style;

import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.component.format.CategoryNameFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.PercentFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.SeriesNameFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.ValueFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.multilayer.style.AttrTooltipMultiLevelNameFormat;

import javax.swing.*;
import java.awt.*;

/**
 * Created by shine on 2017/2/15.
 */
public class VanChartStructureTooltipContentPane extends VanChartTooltipContentPane {
    public VanChartStructureTooltipContentPane(VanChartStylePane parent, JPanel showOnPane) {
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
                return Inter.getLocText("Plugin-ChartF_Node_Name");
            }
        };
        seriesNameFormatPane = new SeriesNameFormatPaneWithCheckBox(parent, showOnPane){
            @Override
            protected String getCheckBoxText() {
                return Inter.getLocText("Plugin-ChartF_MultiPie_Series_Name");
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
