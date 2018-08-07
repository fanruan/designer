package com.fr.van.chart.custom.other;

import com.fr.chart.chartattr.Plot;

import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.type.CustomStyle;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.other.AutoRefreshPane;
import com.fr.van.chart.designer.other.AutoRefreshPaneWithoutTooltip;
import com.fr.van.chart.designer.other.VanChartInteractivePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by Fangjie on 2016/4/28.
 */
public class VanChartCustomInteractivePane extends VanChartInteractivePane {

    private VanChartCustomPlotHyperlinkPane hyperlinkPane;

    /**
     * 组合图无排序按钮
     * @return
     */
    @Override
    protected Component[][] createToolBarComponents() {
       return createToolBarComponentsWithOutSort();
    }

    protected JPanel createHyperlinkPane() {
        hyperlinkPane = new VanChartCustomPlotHyperlinkPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Insert_Hyperlink"), hyperlinkPane);
    }

    @Override
    protected void populateHyperlink(Plot plot) {
        hyperlinkPane.populateBean(chart);
    }

    @Override
    protected void updateHyperlink(Plot plot){
        hyperlinkPane.updateBean(chart);
    }

    protected AutoRefreshPane getMoreLabelPane(VanChartPlot plot) {
        boolean isLargeModel = largeModel(plot);
        //自定义组合图不支持自动数据点提示
        if (((VanChartCustomPlot)plot).getCustomStyle().equals(CustomStyle.CUSTOM)) {
            return new AutoRefreshPaneWithoutTooltip((VanChart) chart, isLargeModel);
        }
        return new AutoRefreshPane((VanChart) chart, isLargeModel);
    }
}
