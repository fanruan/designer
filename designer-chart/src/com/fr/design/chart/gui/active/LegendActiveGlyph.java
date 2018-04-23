package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.LegendGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetLegendStyleAction;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午3:58
 */
public class LegendActiveGlyph extends ActiveGlyph {
    private LegendGlyph legendGlyph;

    public LegendActiveGlyph(ChartComponent chartComponent, LegendGlyph legendGlyph, Glyph parentGlyph) {
        super(chartComponent, parentGlyph);
        this.legendGlyph = legendGlyph;
    }

    public Glyph getGlyph() {
        return this.legendGlyph;
    }
    
    public void goRightPane() {
        new SetLegendStyleAction(chartComponent).showLegendStylePane();
    }
}