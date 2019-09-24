package com.fr.design.chartx.impl;

import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.chart.impl.AbstractChartWithData;
import com.fr.chartx.attr.ChartProvider;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.ChartImagePane;

/**
 * Created by shine on 2019/09/04.
 */
public class DefaultTypePane<T extends AbstractChartWithData> extends AbstractChartTypePane<T> {

    @Override
    protected String[] getTypeIconPath() {
        return ChartTypeInterfaceManager.getInstance().getDemoImagePath(getPlotID());
    }

    @Override
    protected String[] getTypeTipName() {
        return ChartTypeInterfaceManager.getInstance().getSubName(getPlotID());
    }

    @Override
    public ChartProvider getDefaultChart() {
        return ChartTypeManager.getInstance().getChartTypes(getPlotID())[0];
    }

    @Override
    public String title4PopupWindow() {
        return ChartTypeInterfaceManager.getInstance().getName(getPlotID());
    }

    protected int getSelectIndexInChart(T chart) {
        return 0;
    }

    protected void setSelectIndexInChart(T chart, int index) {
    }

    @Override
    public void populateBean(T ob) {
        if (getTypeIconPath().length > 0) {
            for (ChartImagePane imagePane : typeDemo) {
                imagePane.isPressing = false;
            }
            typeDemo.get(getSelectIndexInChart(ob)).isPressing = true;
            checkDemosBackground();
        }
    }

    @Override
    public void updateBean(T ob) {
        if (getTypeIconPath().length > 0) {
            for (int index = 0, len = typeDemo.size(); index < len; index++) {
                if (typeDemo.get(index).isPressing) {
                    setSelectIndexInChart(ob, index);
                    return;
                }
            }
        }
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[0];
    }

    @Override
    protected String[] getTypeLayoutTipName() {
        return new String[0];
    }

    @Override
    protected String getPlotTypeID() {
        return null;
    }
}
