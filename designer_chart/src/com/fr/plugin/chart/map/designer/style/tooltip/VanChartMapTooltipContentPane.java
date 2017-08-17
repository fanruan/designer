package com.fr.plugin.chart.map.designer.style.tooltip;

import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.base.format.AttrTooltipAreaNameFormat;
import com.fr.plugin.chart.base.format.AttrTooltipMapValueFormat;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.component.format.MapAreaNameFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.PercentFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.SeriesNameFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.ValueFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * Created by Mitisky on 16/5/20.
 */
public class VanChartMapTooltipContentPane extends VanChartTooltipContentPane {
    public VanChartMapTooltipContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane) {
        categoryNameFormatPane = new MapAreaNameFormatPaneWithCheckBox(parent, showOnPane);
        seriesNameFormatPane = new SeriesNameFormatPaneWithCheckBox(parent, showOnPane);
        valueFormatPane = new ValueFormatPaneWithCheckBox(parent, showOnPane);
        percentFormatPane = new PercentFormatPaneWithCheckBox(parent, showOnPane);
    }

    @Override
    protected AttrTooltipContent createAttrTooltip() {
        AttrTooltipContent content = new AttrTooltipContent();
        content.setCategoryFormat(new AttrTooltipAreaNameFormat());
        content.setValueFormat(new AttrTooltipMapValueFormat());
        return content;
    }
}
