package com.fr.design.chart.gui.active;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.chart.chartglyph.TrendLineGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetAnalysisLineStyleAction;

public class TrendLineActiveGlyph extends ActiveGlyph{
	private TrendLineGlyph trendLine;
	public TrendLineActiveGlyph(ChartComponent chartComponent,TrendLineGlyph trendLine, Glyph parentGlyph) {
		super(chartComponent, parentGlyph);
		this.trendLine = trendLine;
	}

	@Override
	public Glyph getGlyph() {
		return this.trendLine;
	}
	
	public void drawAllGlyph(Graphics2D g2d, int resolution){
		Point2D offset4Paint = offset4Paint();
        g2d.translate(offset4Paint.getX(), offset4Paint.getY());
        PlotGlyph plotGlyph = (PlotGlyph)this.parentGlyph;
		List<TrendLineGlyph> list = new ArrayList<TrendLineGlyph>();
		plotGlyph.getAllTrendLineGlyph(list);
		for(int index = 0; index < list.size(); index++){
			list.get(index).draw(g2d, resolution);
		}
		g2d.translate(-offset4Paint.getX(), -offset4Paint.getY());
	};

	@Override
	public void goRightPane() {
		new SetAnalysisLineStyleAction(chartComponent).showAnalysisLineStylePane();
	}

}