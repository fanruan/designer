package com.fr.van.chart.map.designer.data;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.drillmap.data.DrillMapDefinition;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.data.VanMapDefinition;
import com.fr.plugin.chart.type.MapType;

import java.util.List;

/**
 * Created by shine on 2017/8/8.
 */
public class MapDataPaneHelper {
    public static ChartCollection getPointMapChartCollection(ChartCollection chartCollection) {
        try {
            ChartCollection cloneCollection = (ChartCollection) chartCollection.clone();
            Chart chart = cloneCollection.getSelectedChart();

            TopDefinitionProvider definition = chart.getFilterDefinition();
            if (definition != null && definition instanceof VanMapDefinition) {
                chart.setFilterDefinition(((VanMapDefinition) definition).getPointDefinition());
            }

            Plot plot = chart.getPlot();
            if (plot != null && plot instanceof VanChartMapPlot) {
                ((VanChartMapPlot) plot).setMapType(MapType.POINT);
            }
            return cloneCollection;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return chartCollection;
        }
    }

    public static ChartCollection getLineMapChartCollection(ChartCollection chartCollection) {
        try {
            ChartCollection cloneCollection = (ChartCollection) chartCollection.clone();
            Chart chart = cloneCollection.getSelectedChart();

            TopDefinitionProvider definition = chart.getFilterDefinition();
            if (definition != null && definition instanceof VanMapDefinition) {
                chart.setFilterDefinition(((VanMapDefinition) definition).getLineDefinition());
            }

            Plot plot = chart.getPlot();
            if (plot != null && plot instanceof VanChartMapPlot) {
                ((VanChartMapPlot) plot).setMapType(MapType.LINE);
            }
            return cloneCollection;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return chartCollection;
        }
    }


    public static ChartCollection getAreaMapChartCollection(ChartCollection chartCollection) {
        try {
            ChartCollection cloneCollection = (ChartCollection) chartCollection.clone();
            Chart chart = cloneCollection.getSelectedChart();

            TopDefinitionProvider definition = chart.getFilterDefinition();
            if (definition != null && definition instanceof VanMapDefinition) {
                chart.setFilterDefinition(((VanMapDefinition) definition).getAreaDefinition());
            }

            Plot plot = chart.getPlot();
            if (plot != null && plot instanceof VanChartMapPlot) {
                ((VanChartMapPlot) plot).setMapType(MapType.AREA);
            }
            return cloneCollection;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return chartCollection;
        }
    }

    public static ChartCollection getBottomDataDrillMapChartCollection(ChartCollection chartCollection) {
        try {
            ChartCollection cloneCollection = (ChartCollection) chartCollection.clone();
            Chart chart = cloneCollection.getSelectedChart();

            TopDefinitionProvider definition = chart.getFilterDefinition();
            if (definition != null && definition instanceof DrillMapDefinition) {
                chart.setFilterDefinition(((DrillMapDefinition) definition).getBottomDataDefinition());
            }

            Plot plot = chart.getPlot();
            if (plot != null && plot instanceof VanChartDrillMapPlot) {
                List<MapType> list = ((VanChartDrillMapPlot) plot).getLayerMapTypeList();
                MapType mapType = (list != null && list.size() > 0) ? list.get(list.size() - 1) : MapType.AREA;
                ((VanChartMapPlot) plot).setMapType(mapType);
            }
            return cloneCollection;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return chartCollection;
        }
    }

    public static ChartCollection getLayerChartCollection(ChartCollection chartCollection, int level, MapType mapType) {
        if (mapType == null) {
            mapType = MapType.AREA;
        }
        try {
            ChartCollection cloneCollection = (ChartCollection) chartCollection.clone();
            Chart chart = cloneCollection.getSelectedChart();

            TopDefinitionProvider definition = chart.getFilterDefinition();
            if (definition != null && definition instanceof DrillMapDefinition) {
                List<TopDefinitionProvider> list = ((DrillMapDefinition) definition).getEachLayerDataDefinitionList();
                if (list.size() > level) {
                    chart.setFilterDefinition(list.get(level));
                }
            }

            Plot plot = chart.getPlot();
            if (plot != null && plot instanceof VanChartMapPlot) {
                ((VanChartMapPlot) plot).setMapType(mapType);
            }
            return cloneCollection;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return chartCollection;
        }
    }


    public static boolean isFromBottomData(ChartCollection chartCollection) {
        DrillMapDefinition drillMapDefinition = getDrillMapDefinition(chartCollection);
        return drillMapDefinition == null || drillMapDefinition.isFromBottomData();
    }

    public static DrillMapDefinition getDrillMapDefinition(ChartCollection chartCollection) {
        if (chartCollection != null) {
            Chart chart = chartCollection.getSelectedChart();
            if (chart != null) {
                TopDefinitionProvider definitionProvider = chart.getFilterDefinition();
                if (definitionProvider instanceof DrillMapDefinition) {
                    return (DrillMapDefinition) definitionProvider;
                }
            }
        }
        return null;
    }

    public static List<MapType> getDrillMapLayerMapTypeList(ChartCollection chartCollection) {
        if (chartCollection != null) {
            Chart chart = chartCollection.getSelectedChart();
            if (chart != null) {
                Plot plot = chart.getPlot();
                if (plot instanceof VanChartDrillMapPlot) {
                    return ((VanChartDrillMapPlot) plot).getLayerMapTypeList();
                }
            }
        }
        return null;
    }

    public static MapType getPlotMapType(ChartCollection chartCollection) {
        Plot plot = chartCollection.getSelectedChart().getPlot();
        return getPlotMapType(plot);
    }

    public static MapType getPlotMapType(Plot plot) {
        if (plot != null && plot instanceof VanChartMapPlot) {
            return ((VanChartMapPlot) plot).getMapType();
        }
        return MapType.AREA;
    }
}
