package com.fr.design.chart.gui;
import com.fr.base.chart.chartdata.CallbackEvent;

import java.awt.*;

/**
 * Created by hufan on 2016/10/18.
 */
public class ChartGlyphDrawEvent implements CallbackEvent {
    private ChartComponent painter;
    private Graphics2D g2d;

    public ChartGlyphDrawEvent() {
    }

    public ChartGlyphDrawEvent(ChartComponent painter, Graphics2D g2d) {
        this.painter = painter;
        this.g2d = g2d;
    }

    @Override
    public void run() {
        painter.drawChartGlyph(g2d);
    }

    @Override
    public void callback() {
        painter.repaint();
        painter.deleteDrawEvent();
    }
}
