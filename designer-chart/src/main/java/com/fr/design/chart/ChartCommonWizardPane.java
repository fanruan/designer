package com.fr.design.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chartx.attr.ChartProvider;

public abstract class ChartCommonWizardPane extends ChartWizardPane {
	private static final long serialVersionUID = 2467967841657570498L;

	@Override
	public void populate(ChartCollection cc) {
		if (cc == null) {
			return;
		}

		populate(cc.getSelectedChartProvider());
	}

	public abstract void populate(ChartProvider chart);

	public abstract void update(ChartProvider oldChart);

}