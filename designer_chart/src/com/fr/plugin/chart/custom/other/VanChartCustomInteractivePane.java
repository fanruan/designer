package com.fr.plugin.chart.custom.other;

import com.fr.chart.chartattr.Plot;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.type.CustomStyle;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.other.AutoRefreshPane;
import com.fr.plugin.chart.designer.other.AutoRefreshPaneWithoutTooltip;
import com.fr.plugin.chart.designer.other.VanChartInteractivePane;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.*;
import java.awt.*;

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
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("M_Insert-Hyperlink"), hyperlinkPane);
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
