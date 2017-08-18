package com.fr.plugin.chart.custom.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.mainframe.chart.gui.style.series.AbstractPlotSeriesPane;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.custom.CustomPlotDesignerPaneFactory;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.component.VanChartCustomPlotTabPane;
import com.fr.plugin.chart.custom.type.CustomPlotType;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangjie on 2016/4/22.
 */
public class VanChartCustomPlotSeriesTabPane extends VanChartCustomPlotTabPane<VanChartCustomPlot, VanChartCustomPlot> {
    public VanChartCustomPlotSeriesTabPane(VanChartCustomPlot plot, BasicPane parent) {
        super(plot, parent);
    }

    @Override
    protected void initTabTitle() {
        List<VanChartPlot> customPlotList = plot.getCustomPlotList();
        NameArray = new String[Math.min(customPlotList.size(), paneList.size())];
        for (int i = 0; i < customPlotList.size() && i < paneList.size(); i++) {
            JPanel pane = paneList.get(i);
            //获取点的tooltip作为标题
            VanChartPlot vanChartPlot = customPlotList.get(i);
            CustomPlotType plotType = CustomPlotFactory.getCustomType(vanChartPlot);

            NameArray[i] = CustomPlotFactory.getTitle(plotType);
            centerPane.add(pane, NameArray[i]);
        }
    }

    @Override
    protected List<JPanel> initPaneList() {
        /**
         * 獲取不同plot的系列面板
         */
        List<JPanel> paneList = new ArrayList<JPanel>();

        List<VanChartPlot> customPlotList = plot.getCustomPlotList();

        for (int i = 0; i < customPlotList.size(); i++){
            //根据不同的plot创建不同的数据配置界面
            AbstractPlotSeriesPane seriesPane = (AbstractPlotSeriesPane) ChartTypeInterfaceManager.getInstance().getPlotSeriesPane((VanChartStylePane)parent, customPlotList.get(i));
            //根据不同的plot获取不同的控制控制位置界面,从Factory中获取
            BasicBeanPane<Plot> plotPositionPane = CustomPlotDesignerPaneFactory.createCustomPlotPositionPane(customPlotList.get(i));

            VanChartCustomPlotSeriesPane contentPane = new VanChartCustomPlotSeriesPane(plotPositionPane, seriesPane);

            paneList.add(contentPane);
        }

        return paneList;
    }

    @Override
    public void populateBean(VanChartCustomPlot plot) {
        /**
         * 更新各个点的系列界面
         */
        List<VanChartPlot> customPlotList = plot.getCustomPlotList();
        for (int i = 0; i < paneList.size() && i <  customPlotList.size(); i++){
            //获取相应点的属性，并更新界面
            VanChartPlot vanChartPlot = customPlotList.get(i);

            ((VanChartCustomPlotSeriesPane)paneList.get(i)).populateBean(vanChartPlot);
        }

    }

    @Override
    public VanChartCustomPlot updateBean() {
        return null;
    }

    @Override
    public void updateBean(VanChartCustomPlot plot) {
        /**
         * 更新每个图表的数据
         */
        List<VanChartPlot> customPlotList = plot.getCustomPlotList();
        for (int i = 0; i < paneList.size() && i <  customPlotList.size(); i++){
            //获取相应点的属性，并更新界面
            VanChartPlot vanChartPlot = customPlotList.get(i);

            VanChartCustomPlotSeriesPane plotSeriesPane = (VanChartCustomPlotSeriesPane) paneList.get(i);

            plotSeriesPane.updateBean(vanChartPlot);
        }
    }

    @Override
    public boolean accept(Object ob) {
        return false;
    }

    @Override
    public String title4PopupWindow() {
        return null;
    }

    @Override
    public void reset() {

    }
}
