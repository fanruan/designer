package com.fr.design.mainframe.chart.gui.style.series;

import javax.swing.*;

import com.fr.chart.chartattr.Bar3DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;

import java.awt.*;

/**
 * 属性表, 三维柱形, 系列界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-9 上午10:08:35
 */
public class Bar3DSeriesPane extends AbstractPlotSeriesPane {
    private UINumberDragPane seriesGap;
    private UINumberDragPane categoryGap;

    private static final double HUNDRED = 100.0;
    private static final double FIVE = 5.0;

    public Bar3DSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot, false);
    }

    @Override
    protected JPanel getContentInPlotType() {
        JPanel pane = null;
        if (this.plot instanceof Bar3DPlot) {
            Bar3DPlot bar3DPlot = (Bar3DPlot) this.plot;
            if (bar3DPlot.isHorizontalDrawBar()) {
                seriesGap = new UINumberDragPane(-HUNDRED, HUNDRED);
                categoryGap = new UINumberDragPane(0, FIVE * HUNDRED);
                double p = TableLayout.PREFERRED;
                double f = TableLayout.FILL;

                double[] columnSize = {p, f};
                double[] rowSize = {p, p};
                Component[][] components = new Component[][]{
                        new Component[]{new UILabel(Inter.getLocText("FR-Chart-Gap_Series")), seriesGap},
                        new Component[]{new UILabel(Inter.getLocText("FR-Chart-Gap_Category")), categoryGap}
                };

                pane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
            }
        }
        return pane;
    }


    public void populateBean(Plot plot) {
        super.populateBean(plot);
        if(plot instanceof Bar3DPlot) {
            Bar3DPlot barPlot = (Bar3DPlot)plot;

            if(seriesGap != null) {
                seriesGap.populateBean(barPlot.getSeriesOverlapPercent() * HUNDRED);
            }
            if(categoryGap != null) {
                categoryGap.populateBean(barPlot.getCategoryIntervalPercent() * HUNDRED);
            }
        }
    }

    public void updateBean(Plot plot) {
        super.updateBean(plot);
        if(plot instanceof Bar3DPlot) {
            Bar3DPlot barPlot = (Bar3DPlot)plot;
            if(seriesGap != null) {
                barPlot.setSeriesOverlapPercent(seriesGap.updateBean()/HUNDRED);
            }
            if(categoryGap != null) {
                barPlot.setCategoryIntervalPercent(categoryGap.updateBean()/HUNDRED);
            }
        }
    }

}