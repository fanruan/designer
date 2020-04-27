package com.fr.van.chart.map.designer.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chartx.attr.ChartProvider;
import com.fr.chartx.config.info.DataConfig;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.NormalChartDataPane;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
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

        switch (mapType) {
            case AREA:
                ChartCollection areaClone = MapDataPaneHelper.getAreaMapChartCollection(collection);
                contentsPane.populate(areaClone);
                break;
            case POINT:
                ChartCollection pointClone = MapDataPaneHelper.getPointMapChartCollection(collection);
                contentsPane.populate(pointClone);
                break;
            case LINE:
                ChartCollection lineClone = MapDataPaneHelper.getLineMapChartCollection(collection);
                contentsPane.populate(lineClone);
                break;
            case CUSTOM:
                ChartCollection areaClone1 = MapDataPaneHelper.getAreaMapChartCollection(collection);
                ChartCollection pointClone1 = MapDataPaneHelper.getPointMapChartCollection(collection);
                ChartCollection lineClone1 = MapDataPaneHelper.getLineMapChartCollection(collection);
                ((CustomMapChartDataContentsPane) contentsPane).populateAreaMap(areaClone1);
                ((CustomMapChartDataContentsPane) contentsPane).populatePointMap(pointClone1);
                ((CustomMapChartDataContentsPane) contentsPane).populateLineMap(lineClone1);
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
            updateBuryingPoint(collection, vanMapDefinition);
        }
    }

    private void updateBuryingPoint(ChartCollection collection, VanMapDefinition vanMapDefinition) {
        ChartProvider chart = collection.getSelectedChartProvider(ChartProvider.class);
        DataConfig dataConfig;
        switch (mapType) {
            case AREA:
                dataConfig = vanMapDefinition.getAreaDefinition().getBuryingPointDataConfig();
                break;
            case POINT:
                dataConfig = vanMapDefinition.getPointDefinition().getBuryingPointDataConfig();
                break;
            case LINE:
                dataConfig = vanMapDefinition.getLineDefinition().getBuryingPointDataConfig();
                break;
            default:
                dataConfig = vanMapDefinition.getBuryingPointDataConfig();
                break;
        }
        ChartInfoCollector.getInstance().updateChartConfig(chart, ConfigType.DATA, dataConfig);
    }
}
