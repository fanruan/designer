package com.fr.van.chart.map.designer.data.component.report;

import com.fr.plugin.chart.map.data.VanMapReportDefinition;

import javax.swing.JPanel;

/**
 * Created by hufan on 2016/12/21.
 */
public abstract class AbstractLongLatAreaPane extends JPanel {
    public abstract void populate(VanMapReportDefinition vanMapReportDefinition);

    public abstract void update(VanMapReportDefinition vanMapReportDefinition);
}
