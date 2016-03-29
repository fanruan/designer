/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.exporter;

import com.fr.base.chart.BaseChartGlyph;
import com.fr.base.chart.chartdata.BaseTableDefinition;
import com.fr.base.chart.chartdata.ChartData;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartglyph.ChartGlyph;
import com.fr.data.TableDataSource;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.design.mainframe.JChart;
import com.fr.script.Calculator;
import com.fr.stable.Constants;
import com.fr.stable.CoreGraphHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-15
 * Time: 上午10:04
 */
public class ImageExporter4Chart implements Exporter4Chart{
    private int resolution = Constants.DEFAULT_WEBWRITE_AND_SCREEN_RESOLUTION;
    protected Calculator calculator;


    public ImageExporter4Chart() {

    }

    /**
     * 导出
     *
     * @param out  输出流
     * @param chart 图表文件
     * @throws Exception 异常
     */
    public void export(OutputStream out, JChart chart) throws Exception {
        ChartDesigner designer = chart.getChartDesigner();
        int imageWidth = designer.getArea().getCustomWidth();
        int imageHeight = designer.getArea().getCustomHeight();
        BufferedImage image = CoreGraphHelper.createBufferedImage(imageWidth, (int) imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        paintGlyph(g2d,imageWidth,imageHeight,designer);
        ImageIO.write(image, "png", out);
    }

    protected void paintGlyph(Graphics2D g2d,int imageWidth,int imageHeight,ChartDesigner designer){
        if (imageWidth == 0 || imageHeight == 0) {
            return;
        }
        this.calculator = Calculator.createCalculator();
        this.calculator.setAttribute(TableDataSource.class, null);
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, imageWidth,imageHeight);
        ChartCollection chartCollection = (ChartCollection) designer.getTarget().getChartCollection();
        Chart editingChart = chartCollection.getSelectedChart();

        TopDefinitionProvider topDefinition = editingChart.getFilterDefinition();
        ChartData chartData4Glyph = null;
        if (topDefinition instanceof BaseTableDefinition) {
            chartData4Glyph = ((BaseTableDefinition) topDefinition).calcu4ChartData(calculator, editingChart.getDataProcessor());
        }

        if (chartData4Glyph == null) {
            chartData4Glyph = editingChart.defaultChartData();
        }

        BaseChartGlyph chartGlyph = null;
        if (editingChart != null && editingChart.getPlot() != null) {
            chartGlyph = editingChart.createGlyph(chartData4Glyph);
        }
        if (chartGlyph instanceof ChartGlyph) {
            Image glyphImage = ((ChartGlyph) chartGlyph).toImage(imageWidth, imageHeight, resolution);
            g2d.drawImage(glyphImage, 0, 0, null);
        }
    }
}