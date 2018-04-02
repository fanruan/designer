package com.fr.van.chart.designer.other;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.mainframe.chart.gui.ChartOtherPane;


public class VanChartOtherPane extends ChartOtherPane {
    private static final long serialVersionUID = 5538256824676045807L;



    public VanChartOtherPane(){
        super();
    }

    @Override
    protected BasicBeanPane<Chart> createInteractivePane() {
        return new VanChartInteractivePane();
    }

    @Override
    protected BasicBeanPane<Chart> createConditionAttrPane() {
        return new VanChartConditionAttrPane();
    }


    @Override
    public void populate(ChartCollection collection) {
        super.populate(collection);
        this.initAllListeners();
    }


}