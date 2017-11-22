package com.fr.plugin.chart.gantt.designer.style.series;

import com.fr.design.mainframe.backgroundpane.ImageBackgroundQuickPane;
import com.fr.plugin.chart.designer.component.marker.VanChartImageMarkerPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hufan on 2017/1/13.
 */
public class VanChartImageMarkerWithoutWidthAndHeightPane extends VanChartImageMarkerPane {

    @Override
    protected JPanel createContentPane(ImageBackgroundQuickPane imageBackgroundPane, JPanel sizePanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(imageBackgroundPane, BorderLayout.CENTER);
        return panel;
    }
}
