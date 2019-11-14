package com.fr.design.chartx.data.map;

import com.fr.chartx.data.MapChartDataDefinition;
import com.fr.design.chartx.AbstractChartDataPane;
import com.fr.design.chartx.fields.diff.AreaMapCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.AreaMapDataSetFieldsPane;
import com.fr.design.chartx.fields.diff.LineMapCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.LineMapDataSetFieldsPane;
import com.fr.design.chartx.fields.diff.PointMapCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.PointMapDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.type.MapType;
import com.fr.van.chart.map.designer.VanMapAreaPointAndLineGroupPane;

import javax.swing.JPanel;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/12
 */
public class MapChartDataPane extends AbstractChartDataPane<MapChartDataDefinition> {

    private SingleDataPane areaPane;
    private SingleDataPane pointPane;
    private SingleDataPane linePane;

    public MapChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected JPanel createContentPane() {
        MapType mapType = MapType.AREA;
        if (this.getVanChart() != null) {
            VanChartMapPlot mapPlot = this.getVanChart().getPlot();
            mapType = mapPlot == null ? mapType : mapPlot.getMapType();
        }
        switch (mapType) {
            case AREA:
                areaPane = new SingleDataPane(new AreaMapDataSetFieldsPane(), new AreaMapCellDataFieldsPane());
                return areaPane;
            case POINT:
                pointPane = new SingleDataPane(new PointMapDataSetFieldsPane(), new PointMapCellDataFieldsPane());
                return pointPane;
            case LINE:
                linePane = new SingleDataPane(new LineMapDataSetFieldsPane(), new LineMapCellDataFieldsPane());
                return linePane;
            case CUSTOM:
                areaPane = new SingleDataPane(new AreaMapDataSetFieldsPane(), new AreaMapCellDataFieldsPane());
                pointPane = new SingleDataPane(new PointMapDataSetFieldsPane(), new PointMapCellDataFieldsPane());
                linePane = new SingleDataPane(new LineMapDataSetFieldsPane(), new LineMapCellDataFieldsPane());
                return new VanMapAreaPointAndLineGroupPane(areaPane, pointPane, linePane);
            default:
                areaPane = new SingleDataPane(new AreaMapDataSetFieldsPane(), new AreaMapCellDataFieldsPane());
                return areaPane;
        }
    }

    @Override
    protected void populate(MapChartDataDefinition mapChartDataDefinition) {
        if (mapChartDataDefinition == null) {
            return;
        }
        if (areaPane != null) {
            areaPane.populateBean(mapChartDataDefinition.getAreaMapDataDefinition());
        }
        if (pointPane != null) {
            pointPane.populateBean(mapChartDataDefinition.getPointMapDataDefinition());
        }
        if (linePane != null) {
            linePane.populateBean(mapChartDataDefinition.getLineMapDataDefinition());
        }

    }

    @Override
    protected MapChartDataDefinition update() {
        MapChartDataDefinition mapChartDataDefinition = new MapChartDataDefinition();
        if (areaPane != null) {
            mapChartDataDefinition.setAreaMapDataDefinition(areaPane.updateBean());
        }
        if (pointPane != null) {
            mapChartDataDefinition.setPointMapDataDefinition(pointPane.updateBean());
        }
        if (linePane != null) {
            mapChartDataDefinition.setLineMapDataDefinition(linePane.updateBean());
        }
        return mapChartDataDefinition;
    }
}
