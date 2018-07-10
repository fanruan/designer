package com.fr.design.mainframe.chart.gui.style.series;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.fr.chart.chartattr.Donut2DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartBeautyPane;

public class Donut2DSeriesPane extends AbstractPlotSeriesPane{
	
	private ChartBeautyPane stylePane;
	
	private UINumberDragPane innerRadiusPercent;
	private UINumberDragPane seriesGap;
	private UINumberDragPane categoryGap;
	
	private static final double MAXSERIESGAP = 50.0;
	private static final double MAXCATEGORYGAP = 100.0;
    private static final double MINGAP = 0.0;
	private static final double MININNERPERCENT = 10;
	private static final double MAXINNERPERCENT = 90;
	private static final double HUNDRED = 100.0;
	
	public Donut2DSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}
	
	public Donut2DSeriesPane(ChartStylePane parent, Plot plot, boolean custom) {
		super(parent, plot, custom);
	}

	protected JPanel getContentInPlotType() {
		stylePane = new ChartBeautyPane();
		
		seriesGap = new UINumberDragPane(MINGAP, MAXSERIESGAP);
		categoryGap = new UINumberDragPane(MINGAP, MAXCATEGORYGAP);
		innerRadiusPercent = new UINumberDragPane(MININNERPERCENT,MAXINNERPERCENT);

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		
		double[] singleCol = {f};
		double[] singleRow = {p};
		
		double[] columnSize = {p, f};
		double[] rowSize = { p,p,p,p,p};
        Component[][] components = new Component[][]{
        		new Component[]{stylePane, null},
        		new Component[]{new JSeparator(), null},
        		new Component[]{null, TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"InnerRadis"}, 
                		new Component[][]{new Component[]{innerRadiusPercent}}, singleRow, singleCol)},
        		new Component[]{null, TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"FR-Chart-Gap_Series"},
        				new Component[][]{new Component[]{seriesGap}}, singleRow, singleCol)},
                new Component[]{null, TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"FR-Chart-Gap_Category"},
                		new Component[][]{new Component[]{categoryGap}}, singleRow, singleCol)},
        };
	
		return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
	}

	
	public void populateBean(Plot plot) {
		super.populateBean(plot);
		if(plot instanceof Donut2DPlot) {
			Donut2DPlot donutPlot = (Donut2DPlot)plot;
			
			if(stylePane != null) {
				stylePane.populateBean(donutPlot.getPlotStyle());
			}
			
			if(innerRadiusPercent != null){
				innerRadiusPercent.populateBean(donutPlot.getInnerRadiusPercent() * HUNDRED);
			}
			if(seriesGap != null) {
				seriesGap.populateBean(donutPlot.getSeriesGap() * HUNDRED);
			}
			if(categoryGap != null) {
				categoryGap.populateBean(donutPlot.getCategoryGap() * HUNDRED);
			}
		}
	}
	
	public void updateBean(Plot plot) {
		super.updateBean(plot);
		if(plot instanceof Donut2DPlot) {
			Donut2DPlot donutPlot = (Donut2DPlot)plot;
			
			if(stylePane != null) {
				donutPlot.setPlotStyle(stylePane.updateBean());
			}
			
			if(innerRadiusPercent != null){
				donutPlot.setInnerRadiusPercent(innerRadiusPercent.updateBean() / HUNDRED);
			}
			if(seriesGap != null) {
				donutPlot.setSeriesGap(seriesGap.updateBean()/HUNDRED);
			}
			if(categoryGap != null) {
				donutPlot.setCategoryGap(categoryGap.updateBean() / HUNDRED);
			}
		}
	}
	
}