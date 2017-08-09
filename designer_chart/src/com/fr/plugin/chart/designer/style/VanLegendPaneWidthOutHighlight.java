package com.fr.plugin.chart.designer.style;

import javax.swing.*;

/**
 * Created by eason on 2016/12/14.
 */
public class VanLegendPaneWidthOutHighlight extends VanChartPlotLegendPane{

    public VanLegendPaneWidthOutHighlight(){

    }

    public VanLegendPaneWidthOutHighlight(VanChartStylePane parent){
        super(parent);
    }

    protected JPanel createLegendPane(){
        return this.createLegendPaneWithoutHighlight();
    }
}
