package com.fr.plugin.chart.custom.other;

import com.fr.design.dialog.BasicPane;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.component.VanChartCustomPlotTabPane;
import com.fr.plugin.chart.custom.type.CustomPlotType;
import com.fr.plugin.chart.designer.other.VanChartConditionAttrPane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangjie on 2016/4/28.
 */
public class VanChartCustomPlotConditionAttrTabPane extends VanChartCustomPlotTabPane<VanChartCustomPlot, VanChartCustomPlot> {
    public VanChartCustomPlotConditionAttrTabPane(VanChartCustomPlot plot, BasicPane parent) {
        super(plot, parent);
    }

    @Override
    protected void initTabTitle() {
        List<VanChartPlot> customPlotList = plot.getCustomPlotList();
        NameArray = new String[paneList.size()];
        for (int i = 0, j = 0 ; i < paneList.size() && j <  customPlotList.size(); i++, j++) {
            if (customPlotList.get(j).isSupportDataSeriesCondition()) {
                JPanel pane = paneList.get(i);
                //获取点的tooltip作为标题
                VanChartPlot vanChartPlot = customPlotList.get(j);

                CustomPlotType plotType = CustomPlotFactory.getCustomType(vanChartPlot);
                NameArray[i] = CustomPlotFactory.getTitle(plotType);
                centerPane.add(pane, NameArray[i]);
            }else {
                //如果不支持，则i不动
                i -- ;
            }
        }
    }

    @Override
    protected List<JPanel> initPaneList() {
        List<JPanel> paneList = new ArrayList<JPanel>();

        List<VanChartPlot> customPlotList = plot.getCustomPlotList();

        for (int i = 0; i < customPlotList.size(); i++){
            if (customPlotList.get(i).isSupportDataSeriesCondition()) {
                //根据不同的plot创建不同的数据配置界面
                VanChartConditionAttrPane contentPane = new VanChartConditionAttrPane();

                paneList.add(contentPane);
            }
        }

        return paneList;
    }

    @Override
    public void populateBean(VanChartCustomPlot plot) {
        List<VanChartPlot> customPlotList = plot.getCustomPlotList();
        for (int i = 0, j = 0 ; i < paneList.size() && j <  customPlotList.size(); i++, j++){
            if (customPlotList.get(j).isSupportDataSeriesCondition()) {
                //获取相应点的属性，并更新界面
                VanChartPlot vanChartPlot = customPlotList.get(j);

                ((VanChartConditionAttrPane) paneList.get(i)).populateBean(vanChartPlot);
            }else {
                i -- ;
            }
        }
    }

    @Override
    public VanChartCustomPlot updateBean() {
        return null;
    }

    @Override
    public void updateBean(VanChartCustomPlot plot) {
        List<VanChartPlot> customPlotList = plot.getCustomPlotList();
        for (int i = 0, j = 0; i < paneList.size() && j <  customPlotList.size(); i++, j++){
            if (customPlotList.get(j).isSupportDataSeriesCondition()) {
                //获取相应点的属性，并更新界面
                VanChartPlot vanChartPlot = customPlotList.get(j);

                VanChartConditionAttrPane conditionPane = (VanChartConditionAttrPane) paneList.get(i);

                conditionPane.updateBean(vanChartPlot);
            }else {
                i --;
            }
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
