package com.fr.plugin.chart.map.designer.other;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.plugin.chart.designer.other.VanChartConditionAttrContentPane;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.designer.VanMapAreaAndPointGroupPane;
import com.fr.plugin.chart.map.designer.VanMapAreaPointAndLineGroupPane;
import com.fr.plugin.chart.map.designer.other.condition.pane.VanChartBubblePointMapConditionPane;
import com.fr.plugin.chart.map.designer.other.condition.pane.VanChartCommonPointMapConditionPane;
import com.fr.plugin.chart.map.designer.other.condition.pane.VanChartDefaultPointMapConditionPane;
import com.fr.plugin.chart.map.designer.other.condition.pane.VanChartImagePointMapConditionPane;
import com.fr.plugin.chart.map.designer.other.condition.pane.VanChartLineMapConditionPane;
import com.fr.plugin.chart.map.designer.other.condition.pane.VanChartMapConditionPane;
import com.fr.plugin.chart.type.MapMarkerType;
import com.fr.plugin.chart.type.MapType;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 16/5/23.
 * 组合地图时有两个
 */
public class VanChartMapConditionAttrContentPane extends VanChartConditionAttrContentPane {
    private VanChartConditionAttrContentPane pointConditionPane;
    private VanChartConditionAttrContentPane areaConditionPane;
    private VanChartConditionAttrContentPane lineConditionPane;

    private MapType mapType;

    public VanChartMapConditionAttrContentPane() {
    }

    private void reLayoutPaneWithMapType(){
        this.setLayout(new BorderLayout());

        switch (mapType){
            case AREA:
                areaConditionPane = new VanChartConditionAttrContentPane();
                this.add(areaConditionPane, BorderLayout.CENTER);
                break;
            case POINT:
                pointConditionPane = new VanChartConditionAttrContentPane();
                this.add(pointConditionPane, BorderLayout.CENTER);
                break;
            case LINE:
                lineConditionPane = new VanChartConditionAttrContentPane();
                this.add(lineConditionPane, BorderLayout.CENTER);
                break;
            default:
                areaConditionPane = new VanChartConditionAttrContentPane();
                pointConditionPane = new VanChartConditionAttrContentPane();
                lineConditionPane = new VanChartConditionAttrContentPane();

                JPanel groupPane;
                if (mapType == MapType.DRILL_CUSTOM){
                    groupPane = new VanMapAreaAndPointGroupPane(areaConditionPane, pointConditionPane);
                }else {
                    groupPane = new VanMapAreaPointAndLineGroupPane(areaConditionPane, pointConditionPane, lineConditionPane);
                }
                this.add(groupPane, BorderLayout.CENTER);

                break;
        }
    }

    @Override
    public void populateBean(Plot plot, Class<? extends ConditionAttributesPane> showPane) {
        if(plot != null && plot instanceof VanChartMapPlot){
            mapType = ((VanChartMapPlot) plot).getAllLayersMapType();
            reLayoutPaneWithMapType();

            ConditionCollection area = plot.getConditionCollection();
            ConditionCollection point = ((VanChartMapPlot) plot).getPointConditionCollection();
            ConditionCollection line = ((VanChartMapPlot)plot).getLineConditionCollection();
            Class<? extends ConditionAttributesPane> pointClass = getPointMapConditionClassPane((VanChartMapPlot)plot);

            switch (mapType){
                case AREA:
                    areaConditionPane.populateBean(plot, area, VanChartMapConditionPane.class);
                    break;
                case POINT:
                    pointConditionPane.populateBean(plot, point, pointClass);
                    break;
                case LINE:
                    lineConditionPane.populateBean(plot, line, VanChartLineMapConditionPane.class);
                    break;
                default:
                    areaConditionPane.populateBean(plot, area, VanChartMapConditionPane.class);
                    pointConditionPane.populateBean(plot, point, pointClass);
                    lineConditionPane.populateBean(plot, line, VanChartLineMapConditionPane.class);
                    break;
            }
        }
    }

    private Class<? extends ConditionAttributesPane> getPointMapConditionClassPane(VanChartMapPlot plot) {
        MapMarkerType mapMarkerType = plot.getMapMarkerType();
        switch (mapMarkerType){
            case COMMON:
                return VanChartCommonPointMapConditionPane.class;
            case BUBBLE:
                return VanChartBubblePointMapConditionPane.class;
            case IMAGE:
                return VanChartImagePointMapConditionPane.class;
            default:
                return VanChartDefaultPointMapConditionPane.class;
        }
    }

    public void updateBean(Plot plot) {
        if(plot != null && plot instanceof VanChartMapPlot){
            ConditionCollection area = plot.getConditionCollection();
            ConditionCollection point = ((VanChartMapPlot) plot).getPointConditionCollection();
            ConditionCollection line = ((VanChartMapPlot) plot).getLineConditionCollection();
            switch (mapType){
                case AREA:
                    areaConditionPane.update(area);
                    break;
                case POINT:
                    pointConditionPane.update(point);
                    break;
                case LINE:
                    lineConditionPane.update(line);
                    break;
                default:
                    areaConditionPane.update(area);
                    pointConditionPane.update(point);
                    lineConditionPane.update(line);
                    break;
            }
        }
    }
}
