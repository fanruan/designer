package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.general.Inter;

public class CustomChartStyleAxisPane extends TernaryChartStyleAxisPane {
	
    public CustomChartStyleAxisPane(Plot plot) {
		super(plot);
	}

	protected String getValueAxisName() {
    	return Inter.getLocText("Main_Axis");
    }
    
    protected String getSecondValueAxisName() {
    	return Inter.getLocText("Second_Axis");
    }
}