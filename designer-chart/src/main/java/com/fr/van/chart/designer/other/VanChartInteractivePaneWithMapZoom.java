package com.fr.van.chart.designer.other;

import com.fr.plugin.chart.attr.plot.VanChartPlot;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by mengao on 2017/4/7.
 */
public class VanChartInteractivePaneWithMapZoom extends VanChartInteractivePaneWithOutSort {

    //图表缩放新设计 恢复用注释。删除下面方法 createZoomPaneContent。
    @Override
    protected JPanel createZoomPaneContent(JPanel zoomWidgetPane, JPanel zoomGesturePane, JPanel changeEnablePane, JPanel zoomTypePane, VanChartPlot plot) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(zoomWidgetPane, BorderLayout.NORTH);
        panel.add(zoomGesturePane, BorderLayout.CENTER);
        return panel;
    }

    //图表缩放新设计 恢复用注释。取消注释。
//    @Override
//    protected ZoomPane createZoomPane() {
//        return new MapZoomPane();
//    }
}
