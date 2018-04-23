package com.fr.design.mainframe.chart.gui.style.series;


import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartBeautyPane;
import com.fr.general.Inter;

public class Bar2DSeriesPane extends AbstractPlotSeriesPane{
	
	protected ChartBeautyPane stylePane;
	
	private UINumberDragPane seriesGap;
	private UINumberDragPane categoryGap;
	
	private static final double HUNDRED = 100.0;
	private static final double FIVE = 5.0;
	
	public Bar2DSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}
	
	public Bar2DSeriesPane(ChartStylePane parent, Plot plot, boolean custom) {
		super(parent, plot, true);
	}
	
	@Override
	protected JPanel getContentInPlotType() {
		seriesGap = new UINumberDragPane(-HUNDRED, HUNDRED);
		categoryGap = new UINumberDragPane(0, FIVE * HUNDRED);
		stylePane = new ChartBeautyPane();

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		
		double[] columnSize = {p, f};
		double[] rowSize = { p,p,p,p};
        Component[][] components = new Component[][]{
        		new Component[]{stylePane, null},
        		new Component[]{new JSeparator(), null},
        		new Component[]{new UILabel(Inter.getLocText("FR-Chart-Gap_Series")), seriesGap},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Gap_Category")), categoryGap}
        };
		
		return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
	}

	public void populateBean(Plot plot) {
		super.populateBean(plot);
		if(plot instanceof Bar2DPlot) {
			Bar2DPlot barPlot = (Bar2DPlot)plot;
			
			if(stylePane != null) {
				stylePane.populateBean(barPlot.getPlotStyle());
			}
			if(seriesGap != null) {
				seriesGap.populateBean(barPlot.getSeriesOverlapPercent() * HUNDRED);
			}
			if(categoryGap != null) {
				categoryGap.populateBean(barPlot.getCategoryIntervalPercent() * HUNDRED);
			}
		}
	}
	
	public void updateBean(Plot plot) {
		super.updateBean(plot);
		if(plot instanceof Bar2DPlot) {
			Bar2DPlot barPlot = (Bar2DPlot)plot;
			
			if(stylePane != null) {
				barPlot.setPlotStyle(stylePane.updateBean());
			}
			if(seriesGap != null && !barPlot.isStacked()) {
				barPlot.setSeriesOverlapPercent(seriesGap.updateBean()/HUNDRED);
			}
			if(categoryGap != null) {
				barPlot.setCategoryIntervalPercent(categoryGap.updateBean()/HUNDRED);
			}
		}
	}
}