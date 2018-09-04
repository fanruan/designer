package com.fr.extended.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.ChartImagePane;

/**
 * Created by shine on 2018/3/5.
 */
public class ExtendedTypePane<T extends AbstractChart> extends AbstractChartTypePane {

    @Override
    protected String[] getTypeIconPath() {
        return new String[0];
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[0];
    }

    protected int getTypeIndex(T chart) {
        return 0;
    }

    protected void setType(T chart, int index) {
    }

    protected void populate(T chart) {
    }

    protected void update(T chart) {
    }

    @Override
    public void populateBean(Chart chart) {
        if (getTypeIconPath().length > 0) {
            for (ChartImagePane imagePane : typeDemo) {
                imagePane.isPressing = false;
            }
            typeDemo.get(getTypeIndex((T) chart)).isPressing = true;
            checkDemosBackground();
        }
        populate((T) chart);
    }

    @Override
    public void updateBean(Chart chart) {
        update((T) chart);

        if (getTypeIconPath().length > 0) {
            for (int index = 0, len = typeDemo.size(); index < len; index++) {
                if (typeDemo.get(index).isPressing) {
                    setType((T) chart, index);
                    return;
                }
            }
        }
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
