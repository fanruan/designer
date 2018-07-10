package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.TitleGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetTitleStyleAction;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午3:57
 */
public class TextActiveGlyph extends ActiveGlyph {
    private TitleGlyph titleGlyph;

    public TextActiveGlyph(ChartComponent chartComponent, TitleGlyph titleGlyph, Glyph parentGlyph) {
        super(chartComponent, parentGlyph);
        this.titleGlyph = titleGlyph;
    }

    public Glyph getGlyph() {
        return this.titleGlyph;
    }

    public java.awt.Point offset4Paint() {
        return new java.awt.Point(0, 0);
    }
    
    public void goRightPane() {
        new SetTitleStyleAction(chartComponent).showTitlePane();
    }

}