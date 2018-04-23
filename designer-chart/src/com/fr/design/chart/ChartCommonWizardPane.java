package com.fr.design.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;

public abstract class ChartCommonWizardPane extends ChartWizardPane {
	private static final long serialVersionUID = 2467967841657570498L;

	@Override
	public void populate(ChartCollection cc) {
		if (cc == null) {
			return;
		}
		
		populate(cc.getSelectedChart());
	}
	
	public abstract void populate(Chart chart);
	
	public abstract void update(Chart oldChart);

}