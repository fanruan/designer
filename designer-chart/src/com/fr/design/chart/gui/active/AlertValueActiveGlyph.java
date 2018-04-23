package com.fr.design.chart.gui.active;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.ChartAlertValueGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetAnalysisLineStyleAction;

public class AlertValueActiveGlyph extends ActiveGlyph{
	ChartAlertValueGlyph alertValueGlyph;
	
	public AlertValueActiveGlyph(ChartComponent chartComponent,ChartAlertValueGlyph alertLine, Glyph parentGlyph) {
		super(chartComponent, parentGlyph);
		this.alertValueGlyph = alertLine;
	}
	
	public Point2D offset4Paint() {
		Rectangle2D valueAxisBoudns = this.alertValueGlyph.getValueAxisGlyph().getBounds();
		
        return new Point2D.Double(
                this.parentGlyph.getShape().getBounds().getX() + valueAxisBoudns.getX(),
                this.parentGlyph.getShape().getBounds().getY() + valueAxisBoudns.getY()
        );
    }
	
	@Override
	public Glyph getGlyph() {
		return this.alertValueGlyph;
	}

	@Override
	public void goRightPane() {
		new SetAnalysisLineStyleAction(chartComponent).showAnalysisLineStylePane();
	}

}