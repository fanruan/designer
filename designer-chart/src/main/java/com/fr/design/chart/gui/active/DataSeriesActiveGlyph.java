package com.fr.design.chart.gui.active;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.DataSeries;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.module.DesignModuleFactory;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午3:59
 */
public class DataSeriesActiveGlyph extends ActiveGlyph {
    private DataSeries series;

    public DataSeriesActiveGlyph(ChartComponent chartComponent, DataSeries series, Glyph parentGlyph) {
        super(chartComponent, parentGlyph);
        this.series = series;
    }

    public Glyph getGlyph() {
        return this.series;
    }

    /**
     * 界面条状
     */
    public void goRightPane() {
    	if(chartComponent.getEditingChart() == null) {
    		return;
    	}

        DesignModuleFactory.getChartPropertyPane().getChartEditPane().gotoPane(PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_SERIES_TITLE);
    }

	@Override
	public void drawAllGlyph(Graphics2D g2d, int resolution) {
		Point2D offset4Paint = offset4Paint();
        g2d.translate(offset4Paint.getX(), offset4Paint.getY());
        if(this.parentGlyph != null && this.parentGlyph instanceof PlotGlyph){
        	PlotGlyph plotGlyph = (PlotGlyph)this.parentGlyph;
        	plotGlyph.drawShape4Series(g2d, resolution);
        }
        g2d.translate(-offset4Paint.getX(), -offset4Paint.getY());
	}

}