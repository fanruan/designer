package com.fr.design.chart.gui.chartimage;

import com.fr.base.chart.BaseChartGlyph;

import java.awt.*;

/**
 * Created by hufan on 2016/10/18.
 */
public class ChartGlyphToImageManager {
    private static ChartGlyphToImageManager toImageManager = new ChartGlyphToImageManager();

    public static Image toImage(Component listener, BaseChartGlyph glyph, int width, int height, int resolution){
        //初始化新的图片处理
        ChartGlyphToImageEvent imageEvent = toImageManager.initImageEvent(listener);
        //获取图片
        return imageEvent.setImageArgs(glyph, width, height, resolution).run();
    }

    private ChartGlyphToImageEvent initImageEvent(Component listener){
       return new ChartGlyphToImageEvent(listener);
    }
}
