package com.fr.van.chart.custom.other;

import com.fr.design.dialog.BasicPane;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.type.CustomPlotType;
import com.fr.van.chart.custom.component.VanChartCustomPlotTabPane;
import com.fr.van.chart.custom.component.VanChartHyperLinkPane;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangjie on 2016/4/28.
 */
public class VanChartCustomPlotHyperlinkTabPane extends VanChartCustomPlotTabPane<VanChartCustomPlot, VanChartCustomPlot> {
    public VanChartCustomPlotHyperlinkTabPane(VanChartCustomPlot plot, BasicPane parent) {
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
            String name = CustomPlotFactory.getTitle(plotType);
            NameArray[i] = name.length() > 3 ? name.substring(0, 3) : name;
            centerPane.add(pane, NameArray[i]);
        }
    }

    @Override
    protected List<JPanel> initPaneList() {
        List<JPanel> paneList = new ArrayList<JPanel>();

        List<VanChartPlot> customPlotList = plot.getCustomPlotList();

        for (int i = 0; i < customPlotList.size(); i++){
            //根据不同的plot创建不同的数据配置界面
            VanChartHyperLinkPane contentPane = new VanChartHyperLinkPane();

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
            ((VanChartHyperLinkPane)paneList.get(i)).populate(vanChartPlot);
        }
    }

    @Override
    public VanChartCustomPlot updateBean() {
        return null;
    }

    @Override
    public void updateBean(VanChartCustomPlot plot) {
        List<VanChartPlot> customPlotList = plot.getCustomPlotList();
        for (int i = 0; i < paneList.size() && i <  customPlotList.size(); i++){
            //获取相应点的属性，并更新界面
            VanChartPlot vanChartPlot = customPlotList.get(i);
            ((VanChartHyperLinkPane)paneList.get(i)).update(vanChartPlot);
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
