package com.fr.van.chart.custom.other;

import com.fr.chart.chartattr.Chart;
import com.fr.design.dialog.BasicScrollPane;

import com.fr.plugin.chart.custom.VanChartCustomPlot;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by Fangjie on 2016/4/28.
 */
public class VanChartCustomConditionAttrPane extends BasicScrollPane<Chart> {
    private VanChartCustomPlotConditionAttrTabPane conditionAttrPane;
    private Chart chart;
    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if(chart == null) {
            return contentPane;
        }
        initConditionAttrPane((VanChartCustomPlot) chart.getPlot());
        if(conditionAttrPane != null) {
            contentPane.add(conditionAttrPane, BorderLayout.CENTER);
        }
        return contentPane;
    }

    private void initConditionAttrPane(VanChartCustomPlot plot) {
        conditionAttrPane = new VanChartCustomPlotConditionAttrTabPane(plot, null);
    }

    @Override
    public void populateBean(Chart chart) {
        this.chart = chart;
        if(conditionAttrPane == null) {
            this.remove(leftcontentPane);
            layoutContentPane();
        }
        if(conditionAttrPane != null) {
            conditionAttrPane.populateBean((VanChartCustomPlot)chart.getPlot());
        }
    }

    @Override
    public void updateBean(Chart chart) {

        if (chart == null){
            return;
        }

        if (conditionAttrPane != null) {
            conditionAttrPane.updateBean((VanChartCustomPlot) chart.getPlot());
        }
    }

    /**
     * 是否显示滚动条
     * @return
     */
    @Override
    protected boolean isShowScrollBar() {
        return false;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Condition_Display");
    }
}
