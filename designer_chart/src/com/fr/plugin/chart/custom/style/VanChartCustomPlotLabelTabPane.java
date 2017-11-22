package com.fr.plugin.chart.custom.style;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.dialog.BasicPane;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.component.VanChartCustomPlotTabPane;
import com.fr.plugin.chart.custom.type.CustomPlotType;
import com.fr.plugin.chart.designer.PlotFactory;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.label.VanChartPlotLabelPane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangjie on 2016/4/22.
 */
public class VanChartCustomPlotLabelTabPane extends VanChartCustomPlotTabPane<VanChartCustomPlot, VanChartCustomPlot> {
    public VanChartCustomPlotLabelTabPane(VanChartCustomPlot plot, BasicPane parent) {
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
        List<JPanel> paneList = new ArrayList<JPanel>();

        List<VanChartPlot> customPlotList = plot.getCustomPlotList();

        for (int i = 0; i < customPlotList.size(); i++){
            //根据不同的plot创建不同的数据配置界面
            VanChartPlotLabelPane contentPane = PlotFactory.createPlotLabelPane(customPlotList.get(i), (VanChartStylePane) parent);

            paneList.add(contentPane);
        }

        return paneList;
    }

    @Override
    public void populateBean(VanChartCustomPlot plot) {
        List<VanChartPlot> customPlotList = plot.getCustomPlotList();
        for (int i = 0; i < paneList.size() && i <  customPlotList.size(); i++){
            //获取相应点的属性，并更新界面
            VanChartPlot vanChartPlot = customPlotList.get(i);
            DataSeriesCondition attr = vanChartPlot.getAttrLabelFromConditionCollection();
            ((VanChartPlotLabelPane)paneList.get(i)).populate((AttrLabel) attr);
        }
    }

    @Override
    public VanChartCustomPlot updateBean() {
        return null;
    }

    @Override
    public void updateBean(VanChartCustomPlot plot) {

        if (plot == null){
            return;
        }

        List<VanChartPlot> customPlotList = plot.getCustomPlotList();
        for (int i = 0; i < paneList.size() && i < customPlotList.size(); i++) {
            ConditionAttr attrList = customPlotList.get(i).getConditionCollection().getDefaultAttr();
            DataSeriesCondition attr = customPlotList.get(i).getAttrLabelFromConditionCollection();
            if (attr != null) {
                attrList.remove(attr);
            }

            VanChartPlotLabelPane labelPane = (VanChartPlotLabelPane) paneList.get(i);
            AttrLabel attrLabel = labelPane.update();

            if (attrLabel != null) {
                attrList.addDataSeriesCondition(attrLabel);
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
