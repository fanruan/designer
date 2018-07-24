package com.fr.van.chart.designer.data;

import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.MeterPlotReportDataContentPane;



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
        return com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Series_Name");
    }

    @Override
    protected String getNValueString() {
        return com.fr.design.i18n.Toolkit.i18nText("Chart-Series_Value");
    }
}
