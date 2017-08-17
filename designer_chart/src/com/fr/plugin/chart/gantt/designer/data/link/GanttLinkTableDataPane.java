package com.fr.plugin.chart.gantt.designer.data.link;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.TableDataPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;

/**
 * Created by hufan on 2017/1/11.
 */
public class GanttLinkTableDataPane extends TableDataPane {
    public GanttLinkTableDataPane(ChartDataPane parent) {
        super(parent);
    }

    @Override
    protected AbstractTableDataContentPane getContentPane(Plot plot) {
        return new GanttLinkTableDataContentPane();
    }
}
