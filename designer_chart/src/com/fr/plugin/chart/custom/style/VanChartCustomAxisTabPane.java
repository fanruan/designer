package com.fr.plugin.chart.custom.style;

import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.custom.CustomPlotDesignerPaneFactory;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.component.VanChartCustomPlotAxisPane;
import com.fr.plugin.chart.custom.component.VanChartCustomPlotTabPane;
import com.fr.plugin.chart.custom.type.CustomPlotType;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.axis.VanChartAxisPane;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangjie on 2016/4/27.
 */
public class VanChartCustomAxisTabPane extends VanChartCustomPlotTabPane<VanChartCustomPlot, VanChartCustomPlot> {

    public VanChartCustomAxisTabPane(VanChartCustomPlot plot, BasicPane parent) {
        super(plot, parent);
    }

    @Override
    protected List<JPanel> initPaneList() {
        List<JPanel> paneList = new ArrayList<JPanel>();

        VanChartCustomPlot customPlot = plot;

        List<VanChartPlot> customPlotList = customPlot.getCustomPlotList();

        /**
         * 判断是否有使用标准坐标轴的点
         */
        ArrayList<Integer> plotOrder = customPlot.getDiffAxisOrder();

        for (int i = 0; i < customPlotList.size(); i++){
            //根据不同的plotOrder创建不同的数据配置界面
            if (plotOrder.contains(i)) {
                VanChartAxisPane contentPane = CustomPlotDesignerPaneFactory.createAxisPane((VanChartAxisPlot) customPlotList.get(i), (VanChartStylePane) parent);
                paneList.add(contentPane);
            }
        }

        //判断是否使用标准坐标轴系，标准坐标系即使用customPlot的坐标轴属性,如果有，放在最后
        if (customPlot.isHaveStandardAxis()){
            VanChartAxisPane contentPane = new VanChartCustomPlotAxisPane(customPlot, (VanChartStylePane) parent);
            paneList.add(contentPane);
        }

        return paneList;
    }

    @Override
    protected void initTabTitle(){
        VanChartCustomPlot customPlot = plot;
        List<VanChartPlot> customPlotList = customPlot.getCustomPlotList();
        ArrayList<Integer> plotOrder = customPlot.getDiffAxisOrder();

        NameArray = new String[Math.min(customPlotList.size(), paneList.size())];


        for (int i = 0;i < paneList.size() && i < plotOrder.size(); i++) {
            JPanel pane = paneList.get(i);

            VanChartPlot vanChartPlot = customPlotList.get(plotOrder.get(i));
            CustomPlotType plotType = CustomPlotFactory.getCustomType(vanChartPlot);

            NameArray[i] = CustomPlotFactory.getTitle(plotType);
            centerPane.add(pane, NameArray[i]);
        }

        //如果有标准坐标系，则放在最后一个tab按钮上
        if (customPlot.isHaveStandardAxis()){
            JPanel pane = paneList.get(paneList.size() - 1);

            //获取点的tooltip作为标题
            NameArray[paneList.size() - 1] = Inter.getLocText("Plugin-ChartF_Rectangular_Coordinate_System");
            centerPane.add(pane, NameArray[paneList.size() - 1]);
        }
    }

    @Override
    /**
     * 标准坐标轴属性存在最外层的customPlot的坐标轴变量中
     * 不使用标准坐标轴的坐标轴属性存在相应的plot中的坐标轴变量中
     */
    public void populateBean(VanChartCustomPlot plot) {
        List<VanChartPlot> customPlotList = plot.getCustomPlotList();

        ArrayList<Integer> plotOrder = plot.getDiffAxisOrder();

        for (int i = 0; i < paneList.size() && i < plotOrder.size(); i++){
            //先更新标准坐标轴界面
            //获取相应点的属性，并更新界面
            VanChartPlot vanChartPlot = customPlotList.get(plotOrder.get(i));
            ((VanChartAxisPane)paneList.get(i)).populateBean(vanChartPlot);
        }

        if (plot.isHaveStandardAxis()){
            ((VanChartAxisPane)paneList.get(paneList.size() - 1)).populateBean(plot);

        }
    }

    @Override
    public VanChartCustomPlot updateBean() {
        return null;
    }

    @Override
    public void updateBean(VanChartCustomPlot plot) {
        VanChartCustomPlot customPlot = plot;
        List<VanChartPlot> customPlotList = customPlot.getCustomPlotList();

        ArrayList<Integer> plotOrder = customPlot.getDiffAxisOrder();

        for (int i = 0; i < paneList.size() && i < plotOrder.size(); i++){
            //获取相应点的属性，并更新界面
            VanChartPlot vanChartPlot = customPlotList.get(plotOrder.get(i));
            ((VanChartAxisPane)paneList.get(i)).updateBean(vanChartPlot);
        }

        if (customPlot.isHaveStandardAxis()){
            //用VanChart包裝plot
            VanChart chart = new VanChart();
            chart.setPlot(customPlot);
            ((VanChartAxisPane)paneList.get(paneList.size() - 1)).updateBean(chart);

        }

        //将标准坐标轴属性存入使用标准坐标轴的点的坐标轴变量中
        CustomPlotFactory.axisSynchronization(plot);
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
