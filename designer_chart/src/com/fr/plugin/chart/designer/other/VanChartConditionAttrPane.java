package com.fr.plugin.chart.designer.other;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.AbstractVanChartScrollPane;

import javax.swing.*;

//条件属性
public class VanChartConditionAttrPane extends AbstractVanChartScrollPane<Chart> {
    private static final long serialVersionUID = 5725969986029470291L;
    private VanChartConditionAttrContentPane conditionAttrContentPane;

    public VanChartConditionAttrPane() {
        super();
    }

    @Override
    protected JPanel createContentPane() {
        if (conditionAttrContentPane == null) {
            conditionAttrContentPane = createConditionAttrContentPane();
        }

        return conditionAttrContentPane;
    }

    protected VanChartConditionAttrContentPane createConditionAttrContentPane() {
        return new VanChartConditionAttrContentPane();
    }

    /**
     * 界面标题
     * @return 标题
     */
    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Chart-Condition_Display");
    }

    @Override
    public void populateBean(Chart chart) {
        Plot plot = chart.getPlot();

        Class<? extends ConditionAttributesPane> showPane  = getPlotConditionPane(chart);

        populate(plot, showPane);
    }

    private void populate(Plot plot, Class<? extends ConditionAttributesPane> showPane) {
        conditionAttrContentPane.populateBean(plot, showPane);
    }

    public void populateBean(Plot plot) {

        Class<? extends ConditionAttributesPane> showPane  = getPlotConditionPane(plot);

        populate(plot, showPane);
    }

    protected Class<? extends ConditionAttributesPane> getPlotConditionPane(Chart chart) {
        return getPlotConditionPane(chart.getPlot());
    }

    private Class<? extends ConditionAttributesPane> getPlotConditionPane(Plot plot) {
        return ChartTypeInterfaceManager.getInstance().getPlotConditionPane(plot).getClass();
    }

    @Override
    public void updateBean(Chart chart) {
        if (chart == null){
            return;
        }

        Plot plot = chart.getPlot();

        updateBean(plot);

    }

    public void updateBean(Plot plot) {
       conditionAttrContentPane.updateBean(plot);
    }
}