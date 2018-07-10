package com.fr.van.chart.funnel.designer.style;

import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.base.format.AttrTooltipNameFormat;
import com.fr.van.chart.designer.component.VanChartTooltipContentPane;
import com.fr.van.chart.designer.component.format.CategoryNameFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.SeriesNameFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.ValueFormatPaneWithCheckBox;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by Mitisky on 16/10/10.
 */
public class VanChartFunnelTooltipContentPane extends VanChartTooltipContentPane {
    public VanChartFunnelTooltipContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    protected double[] getRowSize(double p){
        return new double[]{p,p,p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{seriesNameFormatPane,null},
                new Component[]{valueFormatPane,null},
                new Component[]{percentFormatPane,null},
        };
    }

    @Override
    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane) {
        categoryNameFormatPane = new CategoryNameFormatPaneWithCheckBox(parent, showOnPane);
        seriesNameFormatPane = new SeriesNameFormatPaneWithCheckBox(parent, showOnPane);
        valueFormatPane = new ValueFormatPaneWithCheckBox(parent, showOnPane);
        percentFormatPane = new FunnelPercentFormatPaneWithCheckBox(parent, showOnPane);
    }

    protected AttrTooltipContent createAttrTooltip() {
        AttrTooltipContent attrTooltipContent = new AttrTooltipContent();
        attrTooltipContent.getCategoryFormat().setEnable(false);
        attrTooltipContent.setSeriesFormat(new AttrTooltipNameFormat());
        attrTooltipContent.getSeriesFormat().setEnable(true);
        attrTooltipContent.getValueFormat().setEnable(true);
        return attrTooltipContent;
    }
}
