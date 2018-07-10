package com.fr.design.mainframe.chart.gui.style.series;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.fr.chart.chartattr.Area3DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;

/**
 * 属性表, 三维面积图 图表样式-系列界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-9 上午10:31:47
 */
public class Area3DSeriesPane extends AbstractPlotSeriesPane {
	
	private UINumberDragPane gapPane;
	private static final double HUNDRED = 100.0;

	public Area3DSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}

	@Override
	protected JPanel getContentInPlotType() {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		
		pane.add(new BoldFontTextLabel(Inter.getLocText("Pitch_Percentage") + ":"), BorderLayout.WEST);
		pane.add(gapPane = new UINumberDragPane(0, 500), BorderLayout.CENTER);
		gapPane.populateBean(100.0);
		
		return pane;
	}

	public void populateBean(Plot plot) {
		if(plot instanceof Area3DPlot) {
			Area3DPlot area3D = (Area3DPlot)plot;
			
			super.populateBean(plot);
			gapPane.populateBean(area3D.getSeriesIntervalPercent() * HUNDRED);
		}
	}
	
	public void updateBean(Plot plot) {
		if(plot instanceof Area3DPlot) {
			Area3DPlot area3D = (Area3DPlot)plot;
			super.updateBean(plot);
			
			area3D.setSeriesIntervalPercent(gapPane.updateBean() / HUNDRED);
		}
	}
}