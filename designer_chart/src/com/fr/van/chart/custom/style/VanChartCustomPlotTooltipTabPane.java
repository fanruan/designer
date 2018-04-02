package com.fr.van.chart.custom.style;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.dialog.BasicPane;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.type.CustomPlotType;
import com.fr.plugin.chart.scatter.attr.ScatterAttrTooltip;
import com.fr.van.chart.custom.component.VanChartCustomPlotTabPane;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotTooltipPane;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangjie on 2016/4/22.
 */
public class VanChartCustomPlotTooltipTabPane extends VanChartCustomPlotTabPane<VanChartCustomPlot, VanChartCustomPlot> {
    public VanChartCustomPlotTooltipTabPane(VanChartCustomPlot plot, BasicPane parent) {
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
            VanChartPlotTooltipPane contentPane = PlotFactory.createPlotTooltipPane(customPlotList.get(i), (VanChartStylePane) parent);
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
            DataSeriesCondition attr = vanChartPlot.getAttrTooltipFromConditionCollection();
            ((VanChartPlotTooltipPane)paneList.get(i)).populate((AttrTooltip) attr);
        }
    }

    @Override
    public VanChartCustomPlot updateBean() {
        return null;
    }

    @Override
    public void updateBean(VanChartCustomPlot plot) {
        if(plot == null) {
            return;
        }

        List<VanChartPlot> customPlotList = plot.getCustomPlotList();
        for (int i = 0; i < paneList.size() && i <  customPlotList.size(); i++){
            //获取相应点的属性，并更新界面
            VanChartPlot vanChartPlot = customPlotList.get(i);

            ConditionAttr attrList = vanChartPlot.getConditionCollection().getDefaultAttr();

            //移除所有数据点提示相关属性
            if(attrList.getExisted(AttrTooltip.class) != null){
                attrList.remove(AttrTooltip.class);
            }else if (attrList.getExisted(ScatterAttrTooltip.class) != null){
                attrList.remove(ScatterAttrTooltip.class);
            }

            VanChartPlotTooltipPane tooltipPane = (VanChartPlotTooltipPane) paneList.get(i);

            AttrTooltip attrTooltip = tooltipPane.update();
            if (attrTooltip != null) {
                attrList.addDataSeriesCondition(attrTooltip);
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
