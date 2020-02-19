package com.fr.van.chart.map.designer.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.DataContentsPane;
import com.fr.design.mainframe.chart.gui.data.NormalChartDataPane;
import com.fr.van.chart.map.designer.VanMapAreaPointAndLineGroupPane;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/5/17.
 */
public class CustomMapChartDataContentsPane extends DataContentsPane {
    private NormalChartDataPane areaMapChartDataPane;
    private NormalChartDataPane pointMapChartDataPane;
    private NormalChartDataPane lineMapChartDataPane;

    private AttributeChangeListener listener;
    private ChartDataPane parent;

    public CustomMapChartDataContentsPane(AttributeChangeListener listener, ChartDataPane parent) {
        this.listener = listener;
        this.parent = parent;
        initAll();
    }

    @Override
    public void setSupportCellData(boolean supportCellData) {
        areaMapChartDataPane.setSupportCellData(supportCellData);
        pointMapChartDataPane.setSupportCellData(supportCellData);
        lineMapChartDataPane.setSupportCellData(supportCellData);
    }

    @Override
    public void populate(ChartCollection collection) {
        populateAreaMap(collection);
        populatePointMap(collection);
        populateLineMap(collection);
    }

    @Override
    public void update(ChartCollection collection) {
        updatePointMap(collection);
        updateAreaMap(collection);
        updateLineMap(collection);
    }

    public void populatePointMap(ChartCollection collection) {
        pointMapChartDataPane.populate(collection);
    }

    public void populateLineMap(ChartCollection collection) {
        lineMapChartDataPane.populate(collection);
    }

    public void updatePointMap(ChartCollection collection) {
        pointMapChartDataPane.update(collection);
    }

    public void populateAreaMap(ChartCollection collection) {
        areaMapChartDataPane.populate(collection);
    }

    public void updateAreaMap(ChartCollection collection) {
        areaMapChartDataPane.update(collection);
    }

    public void updateLineMap(ChartCollection collection) {
        lineMapChartDataPane.update(collection);
    }

    @Override
    protected JPanel createContentPane() {
        areaMapChartDataPane = new NormalChartDataPane(listener, parent);
        pointMapChartDataPane = new NormalChartDataPane(listener, parent);
        lineMapChartDataPane = new NormalChartDataPane(listener, parent);

        return new VanMapAreaPointAndLineGroupPane(areaMapChartDataPane, pointMapChartDataPane, lineMapChartDataPane);
    }


}
