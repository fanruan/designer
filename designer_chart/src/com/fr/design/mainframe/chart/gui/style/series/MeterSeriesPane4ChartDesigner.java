package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class MeterSeriesPane4ChartDesigner extends MeterSeriesPane {

    public MeterSeriesPane4ChartDesigner(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected UIColorPickerPane createColorPickerPane(){
        return new UIColorPickerPane("meterString");
    }
}