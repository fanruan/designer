package com.fr.design.chart.gui.chartimage;
import com.fr.base.chart.BaseChartGlyph;
import com.fr.base.chart.chartdata.CallbackEvent;

import java.awt.*;

/**
 * Created by hufan on 2016/10/18.
 */
public class ChartGlyphToImageEvent implements CallbackEvent<Image> {
    private Component painter;
    private BaseChartGlyph glyph;
    private int width;
    private int height;
    private int resolution;

    private Image image;

    public ChartGlyphToImageEvent() {
    }

    public ChartGlyphToImageEvent(Component painter) {
        this.painter = painter;
    }

    public ChartGlyphToImageEvent setImageArgs(BaseChartGlyph glyph, int width, int height, int resolution){
        this.glyph = glyph;
        this.width = width;
        this.height = height;
        this.resolution = resolution;
        return this;
    }

    public Image run() {
        glyph.addChartDataEvent(this);
        this.image =  glyph.toImage(width, height, resolution);
        return image;
    }

    public void callback() {
        painter.repaint();
    }
}
