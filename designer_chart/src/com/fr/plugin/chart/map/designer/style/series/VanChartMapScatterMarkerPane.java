package com.fr.plugin.chart.map.designer.style.series;

import com.fr.chart.chartglyph.Marker;
import com.fr.plugin.chart.marker.type.MarkerType;
import com.fr.plugin.chart.designer.component.marker.VanChartCommonMarkerPane;

import java.awt.*;

/**
 * Created by Mitisky on 16/5/19.
 * 只有标记点类型和半径
 */
public class VanChartMapScatterMarkerPane extends VanChartCommonMarkerPane {
    @Override
    protected Component[][] getUseComponent() {
        return super.getUseComponentWithOutFillColor();
    }

    @Override
    protected Marker[] getMarkers() {
        return getMapScatterMarkers();
    }
}
