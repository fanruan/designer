package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chartx.attr.ChartProvider;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.mainframe.chart.gui.ChartDataPane;

import java.awt.BorderLayout;
import java.util.ArrayList;

/**
 * Created by mengao on 2017/5/3.
 */
public class ChartsEditPane extends ChartEditPane {

    protected ChartsConfigPane chartsConfigPane;

    public ChartsEditPane() {
        this.setLayout(new BorderLayout());
        paneList = new ArrayList<AbstractChartAttrPane>();

        dataPane4SupportCell = new ChartDataPane(listener);
        dataPane4SupportCell.setSupportCellData(true);

        paneList.add(dataPane4SupportCell);

        createTabsPane();
    }

    /**
     * 重新构造面板
     * @param currentChart 图表
     */
    public void reLayout(ChartProvider currentChart) {
        if (currentChart != null) {
            Chart chart = (Chart) currentChart;
            this.removeAll();
            this.setLayout(new BorderLayout());
            paneList = new ArrayList<AbstractChartAttrPane>();

            String plotID = "";
            if (chart.getPlot() != null) {
                plotID = chart.getPlot().getPlotID();
            }

            dataPane4SupportCell = createChartDataPane(plotID);
            chartsConfigPane = ChartTypeInterfaceManager.getInstance().getChartConfigPane(plotID);
            if (dataPane4SupportCell != null) {
                paneList.add(dataPane4SupportCell);
            }
            paneList.add(chartsConfigPane);

            createTabsPane();
        }

    }

}
