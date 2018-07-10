package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetPlotStyleAction;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午3:55
 */
public class PlotActiveGlyph extends ActiveGlyph {
    private PlotGlyph plotGlyph;

    public PlotActiveGlyph(ChartComponent chartComponent, PlotGlyph plotGlyph, Glyph parentGlyph) {
        super(chartComponent, parentGlyph);
        this.plotGlyph = plotGlyph;
    }

    public Glyph getGlyph() {
        return this.plotGlyph;
    }
    
    public void goRightPane() {
        new SetPlotStyleAction(chartComponent).showPlotPane();
    }

    public java.awt.Point offset4Paint() {
        return new java.awt.Point(0, 0);
    }
}