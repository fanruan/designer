package com.fr.plugin.chart.custom.component;

import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.NormalChartDataPane;

import javax.swing.*;

/**
 * Created by Fangjie on 2016/5/18.
 */
public class VanChartDataPane extends ChartDataPane {

    public VanChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected JPanel createContentPane() {
        contentsPane = new NormalChartDataPane(listener, VanChartDataPane.this);
        return contentsPane;
    }
}
