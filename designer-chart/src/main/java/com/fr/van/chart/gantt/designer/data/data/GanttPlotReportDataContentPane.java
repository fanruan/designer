package com.fr.van.chart.gantt.designer.data.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.van.chart.gantt.designer.data.data.component.GanttReportDataContentPane;
import com.fr.van.chart.gantt.designer.data.data.component.GanttReportDataProjectPane;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;

/**
 * Created by hufan on 2017/1/11.
 */
public class GanttPlotReportDataContentPane extends AbstractReportDataContentPane {
    private GanttReportDataProjectPane projectPane;
    private GanttReportDataContentPane contentPane;
    private static final int V_GAP = 7;

    public GanttPlotReportDataContentPane() {
        initComponent();
        this.setLayout(new BorderLayout(0, V_GAP));
        projectPane.setBorder(BorderFactory.createEmptyBorder(0,24,0,15));
        this.add(projectPane, BorderLayout.NORTH);
        this.add(contentPane, BorderLayout.CENTER);
    }

    private void initComponent() {
        projectPane = new GanttReportDataProjectPane();
        contentPane = new GanttReportDataContentPane();
    }
    @Override
    public void populateBean(ChartCollection ob) {
        projectPane.populateBean(ob);
        contentPane.populateBean(ob);
    }

    @Override
    public void updateBean(ChartCollection collection) {
        projectPane.updateBean(collection);
        contentPane.updateBean(collection);
    }

    @Override
    protected String[] columnNames() {
        return new String[0];
    }
}
