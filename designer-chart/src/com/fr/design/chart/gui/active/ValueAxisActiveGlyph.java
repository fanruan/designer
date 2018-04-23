package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.ValueAxisGlyph;
import com.fr.design.chart.gui.ChartComponent;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午4:02
 */
public class ValueAxisActiveGlyph extends AxisActiveGlyph {

    public ValueAxisActiveGlyph(ChartComponent chartComponent, ValueAxisGlyph axis, Glyph parentGlyph) {
        super(chartComponent, axis, parentGlyph);
    }
}