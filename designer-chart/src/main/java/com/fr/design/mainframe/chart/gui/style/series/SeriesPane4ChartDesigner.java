package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class SeriesPane4ChartDesigner extends ChartSeriesPane {
    public SeriesPane4ChartDesigner(ChartStylePane parent) {
        super(parent);
    }

    protected MeterSeriesPane createMeterSeriesPane(ChartStylePane parent,Plot plot){
   		return new MeterSeriesPane4ChartDesigner(parent, plot);
   	}

    protected MapSeriesPane createMapSeriesPane(ChartStylePane parent,Plot plot){
   		return new MapSeriesPane4ChartDesigner(parent, plot);
   	}
}