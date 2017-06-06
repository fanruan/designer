package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.general.Inter;

import javax.swing.*;

/**
 * Created by mengao on 2017/5/16.
 */
public class ChartsConfigPane extends AbstractChartAttrPane {

    public final static String CHART_STYLE_TITLE = Inter.getLocText("Chart-Style_Name");

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
        return "com/fr/design/images/chart/ChartStyle.png";
    }

    @Override
    public String title4PopupWindow() {
        return CHART_STYLE_TITLE;
    }
}
