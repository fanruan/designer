package com.fr.plugin.chart.multilayer.style;

import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.component.format.PercentFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.ValueFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * Created by Fangjie on 2016/6/15.
 */
public class VanChartMultiPieTooltipContentPane extends VanChartTooltipContentPane {
    public VanChartMultiPieTooltipContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane) {
        categoryNameFormatPane = new MultiPieLevelNameFormatPaneWithCheckBox(parent, showOnPane);
        seriesNameFormatPane = new MultiPieSeriesNameFormatPaneWithCheckBox(parent, showOnPane);
        valueFormatPane = new ValueFormatPaneWithCheckBox(parent, showOnPane);
        percentFormatPane = new PercentFormatPaneWithCheckBox(parent, showOnPane);
    }

    @Override
    protected AttrTooltipContent createAttrTooltip() {
        AttrTooltipContent content = new AttrTooltipContent();
        content.setCategoryFormat(new AttrTooltipMultiLevelNameFormat());
        return content;
    }

}
