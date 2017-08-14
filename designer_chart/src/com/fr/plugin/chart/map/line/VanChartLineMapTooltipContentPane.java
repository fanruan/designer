package com.fr.plugin.chart.map.line;

import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.base.format.AttrTooltipStartAndEndNameFormat;
import com.fr.plugin.chart.base.format.AttrTooltipValueFormat;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.component.format.PercentFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.SeriesNameFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.ValueFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * Created by hufan on 2016/12/19.
 */
public class VanChartLineMapTooltipContentPane extends VanChartTooltipContentPane {
    public VanChartLineMapTooltipContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane){
        categoryNameFormatPane = new StartAndEndNameFormatPaneWithCheckBox(parent, showOnPane);
        seriesNameFormatPane = new SeriesNameFormatPaneWithCheckBox(parent, showOnPane);
        valueFormatPane = new ValueFormatPaneWithCheckBox(parent, showOnPane);
        percentFormatPane = new PercentFormatPaneWithCheckBox(parent, showOnPane);
    }

    @Override
    protected AttrTooltipContent createAttrTooltip() {
        AttrTooltipContent content = new AttrTooltipContent();
        content.setCategoryFormat(new AttrTooltipStartAndEndNameFormat());
        content.setValueFormat(new AttrTooltipValueFormat());
        return content;
    }

}
