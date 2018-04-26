package com.fr.design.chart.gui.active;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetAxisStyleAction;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午4:00
 */
public abstract class AxisActiveGlyph extends ActiveGlyph {
    protected AxisGlyph axis;

    public AxisActiveGlyph(ChartComponent chartComponent, AxisGlyph axis, Glyph parentGlyph) {
        super(chartComponent, parentGlyph);
        this.axis = axis;
    }
    
    public void drawAllGlyph(Graphics2D g2d, int resolution){
		Point2D offset4Paint = offset4Paint();
        g2d.translate(offset4Paint.getX(), offset4Paint.getY());
        this.axis.drawWithOutAlert(g2d, resolution);
		g2d.translate(-offset4Paint.getX(), -offset4Paint.getY());
	};
    
    public void goRightPane() {
        new SetAxisStyleAction(chartComponent).showAxisStylePane();
    }

    /**
     * 返回 对应的属性Axis
     */
    public Glyph getGlyph() {
        return this.axis;
    }
}