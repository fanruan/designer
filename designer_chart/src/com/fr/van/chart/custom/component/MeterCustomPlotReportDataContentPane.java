package com.fr.van.chart.custom.component;

import com.fr.chart.chartdata.MeterReportDefinition;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.MeterPlotReportDataContentPane;

import java.awt.Component;

/**
 * Created by Fangjie on 2016/5/23.
 */
public class MeterCustomPlotReportDataContentPane extends MeterPlotReportDataContentPane{
    private UITextField singCateText;
    public MeterCustomPlotReportDataContentPane(ChartDataPane parent) {
        super(parent);
    }

    @Override
    protected Component getSingCatePane() {
       return singCateText = new UITextField();
    }

    @Override
    protected void populateSingCatePane(String name) {
        singCateText.setText(name);
    }

    @Override
    protected void updateSingCatePane(MeterReportDefinition meterDefinition) {

        meterDefinition.setName(singCateText.getText());

    }
}
