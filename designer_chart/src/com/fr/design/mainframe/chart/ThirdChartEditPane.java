package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.mainframe.chart.gui.ChartDataPane;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by mengao on 2017/5/3.
 */
public class ThirdChartEditPane extends ChartEditPane {

    protected ThirdChartConfigPane thirdChartConfigPane;

    public ThirdChartEditPane() {
        this.setLayout(new BorderLayout());
        paneList = new ArrayList<AbstractChartAttrPane>();

        dataPane4SupportCell = new ChartDataPane(listener);
        dataPane4SupportCell.setSupportCellData(true);
        thirdChartConfigPane= new ThirdChartConfigPane();

        paneList.add(dataPane4SupportCell);
        paneList.add(thirdChartConfigPane);

        createTabsPane();
    }

    /**
     * 重新构造面板
     * @param currentChart 图表
     */
    public void reLayout(Chart currentChart) {
        if (currentChart != null) {
            int chartIndex = getSelectedChartIndex(currentChart);
            this.removeAll();
            this.setLayout(new BorderLayout());
            paneList = new ArrayList<AbstractChartAttrPane>();

            String plotID = "";
            if (currentChart.getPlot() != null) {
                plotID = currentChart.getPlot().getPlotID();
            }

            dataPane4SupportCell = createChartDataPane(plotID);
            thirdChartConfigPane= ChartTypeInterfaceManager.getInstance().getChartConfigPane(plotID);
            paneList.add(dataPane4SupportCell);
            paneList.add(thirdChartConfigPane);

            createTabsPane();
        }

    }

}
