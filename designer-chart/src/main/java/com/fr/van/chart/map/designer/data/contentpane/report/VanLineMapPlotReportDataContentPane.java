package com.fr.van.chart.map.designer.data.contentpane.report;

import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.van.chart.map.designer.data.component.report.AbstractLongLatAreaPane;
import com.fr.van.chart.map.designer.data.component.report.LineMapAreaPane;
import com.fr.van.chart.map.designer.data.component.report.LineMapLongLatAreaPane;

/**
 * Created by hufan on 2016/12/15.
 */
public class VanLineMapPlotReportDataContentPane extends VanPointMapPlotReportDataContentPane {

    public VanLineMapPlotReportDataContentPane(ChartDataPane parent) {
        super(parent);
    }

    protected AbstractLongLatAreaPane getAreaPane() {
        return new LineMapAreaPane();
    }

    protected AbstractLongLatAreaPane getLongLatAreaPane() {
        return new LineMapLongLatAreaPane();
    }
}
