package com.fr.design.mainframe.chart.gui.style.series;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import com.fr.base.Utils;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.RangePlot;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;

public class RangeSeriesPane extends AbstractPlotSeriesPane{

	private UIBasicSpinner seriesWidthSpinner;
	
	public RangeSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}

	@Override
	public void populateBean(Plot plot) {
		super.populateBean(plot);
		seriesWidthSpinner.setValue(((RangePlot)plot).getSeriesWidth());
	}

	@Override
	public void updateBean(Plot plot) {
		super.updateBean(plot);
		((RangePlot)plot).setSeriesWidth(Double.valueOf(Utils.objectToString(seriesWidthSpinner.getValue())));
	}

	@Override
	protected JPanel getContentInPlotType() {
		seriesWidthSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		JPanel pane = new JPanel(new BorderLayout());
		pane.add(new BoldFontTextLabel(Inter.getLocText(new String[]{"ChartF-Series", "Tree-Width"})),BorderLayout.WEST);
		pane.add(seriesWidthSpinner, BorderLayout.CENTER);
		return pane;
	}

}