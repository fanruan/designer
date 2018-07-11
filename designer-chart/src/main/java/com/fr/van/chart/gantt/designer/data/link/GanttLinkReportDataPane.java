package com.fr.van.chart.gantt.designer.data.link;

import com.fr.chart.chartattr.Chart;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ReportDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;

/**
 * Created by hufan on 2017/1/12.
 */
public class GanttLinkReportDataPane extends ReportDataPane {
    public GanttLinkReportDataPane(ChartDataPane parent) {
        super(parent);
    }

    @Override
    protected AbstractReportDataContentPane getContentPane(Chart chart) {

        return new GanttLinkReportDataContentPane();
    }
}
