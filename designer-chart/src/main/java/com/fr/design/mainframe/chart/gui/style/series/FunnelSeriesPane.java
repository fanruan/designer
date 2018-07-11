package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Randost
 * Date: 14-12-3
 * Time: 上午10:35
 * To change this template use File | Settings | File Templates.
 */
public class FunnelSeriesPane extends AbstractPlotSeriesPane {

    public FunnelSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot, false);
    }

    @Override
    protected JPanel getContentInPlotType() {
        return null;
    }
}