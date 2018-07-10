package com.fr.design.chart.gui.active;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.chart.chartglyph.TextGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetDataLabelStyleAction;

public class DataLabelActiveGlyph extends ActiveGlyph{
	private TextGlyph dataLabel;
	
	public DataLabelActiveGlyph(ChartComponent chartComponent, TextGlyph dataLabel, Glyph parentGlyph) {
		super(chartComponent, parentGlyph);
		this.dataLabel = dataLabel;
	}
	
	public void drawAllGlyph(Graphics2D g2d, int resolution){
		Point2D offset4Paint = offset4Paint();
        g2d.translate(offset4Paint.getX(), offset4Paint.getY());
    	
        ArrayList<TextGlyph> allDataPointLableGlyph = new ArrayList<TextGlyph>();
        PlotGlyph plotGlyph = (PlotGlyph)(this.parentGlyph);
        plotGlyph.getAllDataPointGlyph(allDataPointLableGlyph);
		for(int index = 0; index < allDataPointLableGlyph.size(); index++){
			allDataPointLableGlyph.get(index).draw(g2d, resolution);
		}
		g2d.translate(-offset4Paint.getX(), -offset4Paint.getY());
	};
	
	@Override
	public Glyph getGlyph() {
		return this.dataLabel;
	}

	@Override
	public void goRightPane() {
		new SetDataLabelStyleAction(chartComponent).showDataLabelStylePane();
    }

}