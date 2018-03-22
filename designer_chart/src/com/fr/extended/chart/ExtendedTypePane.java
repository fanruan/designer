package com.fr.extended.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;

/**
 * Created by shine on 2018/3/5.
 */
public class ExtendedTypePane extends AbstractChartTypePane {

    @Override
    protected String[] getTypeIconPath() {
        return new String[0];
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[0];
    }

    @Override
    public void populateBean(Chart chart) {
    }

    @Override
    public void updateBean(Chart chart) {
    }

    @Override
    protected String getPlotTypeID() {
        return null;
    }

    @Override
    public Chart getDefaultChart() {
        return ChartTypeManager.getInstance().getChartTypes(getPlotID())[0];
    }

    @Override
    public String title4PopupWindow() {
        return ChartTypeManager.getInstance().getChartName(getPlotID());
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[0];
    }

    @Override
    protected String[] getTypeLayoutTipName() {
        return new String[0];
    }
}
