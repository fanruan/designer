package com.fr.plugin.chart.gantt.designer.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.DataContentsPane;

import javax.swing.*;

/**
 * Created by hufan on 2017/1/10.
 */
public class VanChartGanttContentsPane extends DataContentsPane {

    private VanChartGanttDataAndLinkPane dataTabAndTaskLinkPane;
    private AttributeChangeListener listener;
    private ChartDataPane parent;

    public VanChartGanttContentsPane(AttributeChangeListener listener, ChartDataPane parent) {
        this.listener = listener;
        this.parent = parent;
        initAll();
    }

    /**
     * 设置是否关联单元格数据.
     *
     * @param surpportCellData
     */
    @Override
    public void setSupportCellData(boolean surpportCellData) {
        dataTabAndTaskLinkPane.setSupportCellData(surpportCellData);
    }

    @Override
    public void populate(ChartCollection collection) {
        dataTabAndTaskLinkPane.populateBean(collection);
    }

    @Override
    public void update(ChartCollection collection) {
        dataTabAndTaskLinkPane.updateBean(collection);
    }

    @Override
    protected JPanel createContentPane() {
        dataTabAndTaskLinkPane = new VanChartGanttDataAndLinkPane(this.listener, this.parent);
        return dataTabAndTaskLinkPane;
    }
}
