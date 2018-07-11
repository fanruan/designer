package com.fr.van.chart.map.designer.style.series;

import com.fr.chart.chartglyph.Marker;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.marker.VanChartCommonMarkerPane;

import java.awt.Component;

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

    @Override
    protected double[] getcolumnSize () {
        double s = TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH;
        double d = TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH;
        return new double[] {d, s};
    }
}
