package com.fr.plugin.chart.wordcloud.designer.style;

import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.base.format.AttrTooltipNameFormat;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.component.format.CategoryNameFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.PercentFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.SeriesNameFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.ValueFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * Created by Mitisky on 16/11/29.
 */
public class VanChartWordCloudTooltipContentPane extends VanChartTooltipContentPane {
    public VanChartWordCloudTooltipContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane) {
        categoryNameFormatPane = new CategoryNameFormatPaneWithCheckBox(parent, showOnPane){
            @Override
            protected String getCheckBoxText() {
                return Inter.getLocText("Plugin-ChartF_MultiPie_Series_Name");
            }
        };
        seriesNameFormatPane = new SeriesNameFormatPaneWithCheckBox(parent, showOnPane){
            @Override
            protected String getCheckBoxText() {
                return Inter.getLocText("Plugin-ChartF_Word_Name");
            }
        };
        valueFormatPane = new ValueFormatPaneWithCheckBox(parent, showOnPane){
            @Override
            protected String getCheckBoxText() {
                return Inter.getLocText("Plugin-ChartF_Word_Value");
            }
        };
        percentFormatPane = new PercentFormatPaneWithCheckBox(parent, showOnPane);
    }

    @Override
    protected AttrTooltipContent createAttrTooltip() {
        AttrTooltipContent attrTooltipContent = new AttrTooltipContent();
        attrTooltipContent.setSeriesFormat(new AttrTooltipNameFormat());
        return attrTooltipContent;
    }
}
