package com.fr.van.chart.designer.component.tooltip;

import com.fr.van.chart.designer.component.VanChartTooltipContentPane;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * 仪表盘非多指的数据点提示（没有系列名）
 */
public class TooltipContentPaneWithOutSeries extends VanChartTooltipContentPane {

    private static final long serialVersionUID = -1973565663365672717L;

    public TooltipContentPaneWithOutSeries(VanChartStylePane parent, JPanel showOnPane){
        super(parent, showOnPane);
    }

    protected double[] getRowSize(double p){
        return new double[]{p,p,p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{categoryNameFormatPane,null},
                new Component[]{valueFormatPane,null},
                new Component[]{percentFormatPane,null},
        };
    }
}