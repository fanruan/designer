package com.fr.van.chart.map.designer.data;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.NormalChartDataPane;
import com.fr.plugin.chart.map.MapMatchResult;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.data.VanMapDefinition;
import com.fr.plugin.chart.type.MapType;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by Mitisky on 16/5/16.
 */
public class VanChartMapDataPane extends ChartDataPane {
    MapType mapType = MapType.AREA;

    public VanChartMapDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected JPanel createContentPane() {
        contentsPane = new NormalChartDataPane(listener, VanChartMapDataPane.this);
        return contentsPane;
    }

    protected void repeatLayout(ChartCollection collection) {
        if (contentsPane != null) {
            this.remove(contentsPane);
        }
        this.setLayout(new BorderLayout(0, 0));

        switch (mapType) {
            case CUSTOM:
                contentsPane = new CustomMapChartDataContentsPane(listener, VanChartMapDataPane.this);
                break;
            default:
                contentsPane = new NormalChartDataPane(listener, VanChartMapDataPane.this);
                break;
        }

        contentsPane.setSupportCellData(isSupportCellData());

        this.add(contentsPane, BorderLayout.CENTER);
    }

    /**
     * 更新界面 数据内容
     */
    public void populate(ChartCollection collection) {
        mapType = MapDataPaneHelper.getPlotMapType(collection);

        repeatLayout(collection);

        MapMatchResult matchResult = ((VanChartMapPlot) collection.getSelectedChartProvider(Chart.class).getPlot()).getMatchResult();

        ChartCollection areaClone;
        ChartCollection pointClone;
        ChartCollection lineClone;
        switch (mapType) {
            case AREA:
                areaClone = MapDataPaneHelper.getAreaMapChartCollection(collection);
                ((VanChartMapPlot)areaClone.getSelectedChartProvider(Chart.class).getPlot()).setMatchResult(matchResult);
                contentsPane.populate(areaClone);
                break;
            case POINT:
                pointClone = MapDataPaneHelper.getPointMapChartCollection(collection);
                ((VanChartMapPlot)pointClone.getSelectedChartProvider(Chart.class).getPlot()).setMatchResult(matchResult);
                contentsPane.populate(pointClone);
                break;
            case LINE:
                lineClone = MapDataPaneHelper.getLineMapChartCollection(collection);
                ((VanChartMapPlot)lineClone.getSelectedChartProvider(Chart.class).getPlot()).setMatchResult(matchResult);
                contentsPane.populate(lineClone);
                break;
            case CUSTOM:
                areaClone = MapDataPaneHelper.getAreaMapChartCollection(collection);
                pointClone = MapDataPaneHelper.getPointMapChartCollection(collection);
                lineClone = MapDataPaneHelper.getLineMapChartCollection(collection);
                ((VanChartMapPlot)areaClone.getSelectedChartProvider(Chart.class).getPlot()).setMatchResult(matchResult);
                ((VanChartMapPlot)pointClone.getSelectedChartProvider(Chart.class).getPlot()).setMatchResult(matchResult);
                ((VanChartMapPlot)lineClone.getSelectedChartProvider(Chart.class).getPlot()).setMatchResult(matchResult);
                ((CustomMapChartDataContentsPane) contentsPane).populateAreaMap(areaClone);
                ((CustomMapChartDataContentsPane) contentsPane).populatePointMap(pointClone);
                ((CustomMapChartDataContentsPane) contentsPane).populateLineMap(lineClone);
        }

    }

    /**
     * 保存 数据界面内容
     */
    public void update(ChartCollection collection) {
        if (contentsPane != null) {
            VanMapDefinition vanMapDefinition = new VanMapDefinition();

            ChartCollection pointClone = MapDataPaneHelper.getPointMapChartCollection(collection);
            ChartCollection areaClone = MapDataPaneHelper.getAreaMapChartCollection(collection);
            ChartCollection lineClone = MapDataPaneHelper.getLineMapChartCollection(collection);

            switch (mapType) {
                case AREA:
                    contentsPane.update(areaClone);
                    pointClone.getSelectedChart().setFilterDefinition(null);
                    lineClone.getSelectedChart().setFilterDefinition(null);
                    break;
                case POINT:
                    contentsPane.update(pointClone);
                    areaClone.getSelectedChart().setFilterDefinition(null);
                    lineClone.getSelectedChart().setFilterDefinition(null);
                    break;
                case LINE:
                    contentsPane.update(lineClone);
                    areaClone.getSelectedChart().setFilterDefinition(null);
                    pointClone.getSelectedChart().setFilterDefinition(null);
                    break;
                case CUSTOM:
                    ((CustomMapChartDataContentsPane) contentsPane).updateAreaMap(areaClone);
                    ((CustomMapChartDataContentsPane) contentsPane).updatePointMap(pointClone);
                    ((CustomMapChartDataContentsPane) contentsPane).updateLineMap(lineClone);
                    break;
            }
            vanMapDefinition.setAreaDefinition(areaClone.getSelectedChart().getFilterDefinition());
            vanMapDefinition.setPointDefinition(pointClone.getSelectedChart().getFilterDefinition());
            vanMapDefinition.setLineDefinition(lineClone.getSelectedChart().getFilterDefinition());

            collection.getSelectedChart().setFilterDefinition(vanMapDefinition);
        }
    }
}
