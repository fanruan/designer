package com.fr.van.chart.designer.style;

import javax.swing.JPanel;

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
