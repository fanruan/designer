package com.fr.van.chart.drillmap.designer.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.DataContentsPane;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/6/20.
 */
public class VanChartDrillMapContentsPane extends DataContentsPane {

    private VanChartMapLayerAndDataTabPane layerAndDataTabPane;

    private AttributeChangeListener listener;
    private ChartDataPane parent;

    public VanChartDrillMapContentsPane(AttributeChangeListener listener, ChartDataPane parent) {
        this.listener = listener;
        this.parent = parent;
        initAll();
    }

    /**
     * 设置是否关联单元格数据.
     *
     * @param supportCellData
     */
    @Override
    public void setSupportCellData(boolean supportCellData) {
        layerAndDataTabPane.setSupportCellData(supportCellData);
    }

    @Override
    public void populate(ChartCollection collection) {
        layerAndDataTabPane.populateBean(collection);
    }

    @Override
    public void update(ChartCollection collection) {
        layerAndDataTabPane.updateBean(collection);

    }

    @Override
    protected JPanel createContentPane() {
        layerAndDataTabPane = new VanChartMapLayerAndDataTabPane(this.listener, this.parent);
        return layerAndDataTabPane;
    }
}
