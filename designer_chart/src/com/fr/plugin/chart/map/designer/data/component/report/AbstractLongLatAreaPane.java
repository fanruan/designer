package com.fr.plugin.chart.map.designer.data.component.report;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.plugin.chart.map.data.VanMapReportDefinition;

import javax.swing.*;

/**
 * Created by hufan on 2016/12/21.
 */
public abstract class AbstractLongLatAreaPane extends JPanel {
    public abstract void populate(VanMapReportDefinition vanMapReportDefinition);
    public abstract void update(VanMapReportDefinition vanMapReportDefinition);
}
