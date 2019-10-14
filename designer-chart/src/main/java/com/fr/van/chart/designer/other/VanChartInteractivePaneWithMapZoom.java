package com.fr.van.chart.designer.other;

import com.fr.van.chart.designer.other.zoom.MapZoomPane;
import com.fr.van.chart.designer.other.zoom.ZoomPane;

/**
 * Created by mengao on 2017/4/7.
 */
public class VanChartInteractivePaneWithMapZoom extends VanChartInteractivePaneWithOutSort {

    @Override
    protected ZoomPane createZoomPane() {
        return new MapZoomPane();
    }
}
