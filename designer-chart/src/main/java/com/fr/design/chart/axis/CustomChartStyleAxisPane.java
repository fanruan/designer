package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;


public class CustomChartStyleAxisPane extends TernaryChartStyleAxisPane {
	
    public CustomChartStyleAxisPane(Plot plot) {
		super(plot);
	}

	protected String getValueAxisName() {
    	return com.fr.design.i18n.Toolkit.i18nText("Main_Axis");
    }
    
    protected String getSecondValueAxisName() {
    	return com.fr.design.i18n.Toolkit.i18nText("Second_Axis");
    }
}