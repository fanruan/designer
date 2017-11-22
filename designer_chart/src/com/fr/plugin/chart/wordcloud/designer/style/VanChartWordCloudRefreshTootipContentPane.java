package com.fr.plugin.chart.wordcloud.designer.style;

import com.fr.general.Inter;
import com.fr.plugin.chart.designer.component.format.ChangedPercentFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.ChangedValueFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mengao on 2017/6/9.
 */
public class VanChartWordCloudRefreshTootipContentPane extends VanChartWordCloudTooltipContentPane {
    public VanChartWordCloudRefreshTootipContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(null, showOnPane);
    }

    @Override
    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane) {
        super.initFormatPane(parent, showOnPane);

        changedValueFormatPane = new ChangedValueFormatPaneWithCheckBox(parent, showOnPane){
            @Override
            protected String getCheckBoxText() {
                return Inter.getLocText("Plugin-ChartF_Change_Word_Value");
            }
        };

        changedPercentFormatPane = new ChangedPercentFormatPaneWithCheckBox(parent, showOnPane);
    }

    protected double[] getRowSize(double p){
        return new double[]{p,p,p,p,p,p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{categoryNameFormatPane,null},
                new Component[]{seriesNameFormatPane,null},
                new Component[]{valueFormatPane,null},
                new Component[]{changedValueFormatPane,null},
                new Component[]{percentFormatPane,null},
                new Component[]{changedPercentFormatPane,null},
        };
    }
}
