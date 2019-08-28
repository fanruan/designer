package com.fr.van.chart.designer.other.zoom;

import javax.swing.JPanel;

/**
 * Created by shine on 2019/08/28.
 */
public class ZoomPaneWithOutMode extends ZoomPane {
    @Override
    protected JPanel createNorthPane(double f, double p) {
        return null;
    }

    @Override
    protected void initCustomModePane(double[] columnSize, double p) {
    }
}
