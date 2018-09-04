package com.fr.van.chart.gantt.designer.style.series;

import com.fr.base.background.ColorBackground;
import com.fr.chart.chartglyph.Marker;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ColorSelectBoxWithOutTransparent;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.marker.type.MarkerType;
import com.fr.van.chart.designer.component.marker.VanChartCommonMarkerPane;

import java.awt.Color;
import java.awt.Component;

/**
 * Created by hufan on 2017/1/13.
 */
public class VanChartGanttCommonMarkerPane extends VanChartCommonMarkerPane {
    private ColorSelectBoxWithOutTransparent colorSelect;

    @Override
    protected Marker[] getMarkers() {
        return getGanttMarkers();
    }

    @Override
    protected Component[][] getUseComponent() {
        colorSelect = new ColorSelectBoxWithOutTransparent(100);

        return new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Shape")), getMarkerPane()},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color")), colorSelect}
        };
    }

    @Override
    protected MarkerType populateMarkType(VanChartAttrMarker marker){
        return marker.getMarkerType() == MarkerType.MARKER_DIAMOND_HOLLOW ? MarkerType.MARKER_DIAMOND : marker.getMarkerType();
    }

    @Override
    protected void populateColor(VanChartAttrMarker marker) {
        colorSelect.setSelectObject(marker.getColorBackground().getColor());
    }

    @Override
    protected void updateColor(VanChartAttrMarker marker) {
        Color color = colorSelect.getSelectObject();
        color = color == null ? new Color(248, 182, 44) : color;

        marker.setColorBackground(ColorBackground.getInstance(color));
    }

    @Override
    public void setDefaultValue() {
        getMarkerPane().setSelectedMarker(Marker.createMarker(MarkerType.MARKER_STAR));
        colorSelect.setSelectObject(new Color(248, 182, 44));
    }

}
