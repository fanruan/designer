package com.fr.van.chart.designer.data;

import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.MeterPlotReportDataContentPane;
import com.fr.general.Inter;


/**
 * Created by Mitisky on 16/10/25.
 * 一维 单元格数据配置界面.系列名,值.
 */
public class OneDimensionalPlotReportDataContentPane extends MeterPlotReportDataContentPane{
    public OneDimensionalPlotReportDataContentPane(ChartDataPane parent) {
        super(parent);
    }

    @Override
    protected String getCateNameString() {
        return Inter.getLocText("FR-Chart-Series_Name");
    }

    @Override
    protected String getNValueString() {
        return Inter.getLocText("Chart-Series_Value");
    }
}
