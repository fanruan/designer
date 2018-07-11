package com.fr.design.chart.gui.active;

import java.awt.geom.Point2D;

import com.fr.base.chart.BaseChartGlyph;
import com.fr.base.chart.Glyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetChartStyleAciton;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午3:54
 */
public class ChartActiveGlyph extends ActiveGlyph {
    private BaseChartGlyph glyphChart;

    public ChartActiveGlyph(ChartComponent chartComponent, BaseChartGlyph chart) {
        this(chartComponent, chart, null);
    }
    public ChartActiveGlyph(ChartComponent chartComponent, BaseChartGlyph chart, Glyph parentGlyph) {
        super(chartComponent, parentGlyph);
        this.glyphChart = chart;
    }

    public Glyph getGlyph() {
        return this.glyphChart;
    }

    public Point2D offset4Paint() {
        return new java.awt.Point(0, 0);
    }

    public void goRightPane() {
        new SetChartStyleAciton(chartComponent).showChartStylePane();
    }
}