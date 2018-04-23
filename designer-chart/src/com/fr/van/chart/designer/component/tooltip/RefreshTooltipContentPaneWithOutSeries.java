package com.fr.van.chart.designer.component.tooltip;

import com.fr.van.chart.designer.component.format.ChangedPercentFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.ChangedValueFormatPaneWithCheckBox;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by mengao on 2017/6/11.
 */
public class RefreshTooltipContentPaneWithOutSeries extends TooltipContentPaneWithOutSeries {
    public RefreshTooltipContentPaneWithOutSeries(VanChartStylePane parent, JPanel showOnPane) {
        super(null, showOnPane);
    }

    @Override
    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane) {
        super.initFormatPane(parent, showOnPane);

        changedValueFormatPane = new ChangedValueFormatPaneWithCheckBox(parent, showOnPane);
        changedPercentFormatPane = new ChangedPercentFormatPaneWithCheckBox(parent, showOnPane);
    }

    protected double[] getRowSize(double p){
        return new double[]{p,p,p,p,p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{categoryNameFormatPane,null},
                new Component[]{valueFormatPane,null},
                new Component[]{changedValueFormatPane,null},
                new Component[]{percentFormatPane,null},
                new Component[]{changedPercentFormatPane,null},
        };
    }
}
