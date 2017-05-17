package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.ChartCollection;

import javax.swing.*;

/**
 * Created by mengao on 2017/5/16.
 */
public class ThirdChartConfigPane extends AbstractChartAttrPane {
    @Override
    public void populate(ChartCollection collection) {

    }

    @Override
    public void update(ChartCollection collection) {

    }

    @Override
    protected JPanel createContentPane() {
        return new JPanel();
    }

    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/ChartF-Column.png";
    }

    @Override
    public String title4PopupWindow() {
        return "CustomChart";
    }
}
