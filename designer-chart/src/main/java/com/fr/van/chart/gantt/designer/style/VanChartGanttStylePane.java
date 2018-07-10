package com.fr.van.chart.gantt.designer.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.gantt.designer.style.axis.GanttProcessAxisPane;
import com.fr.van.chart.gantt.designer.style.axis.GanttTimeAxisPane;

import java.util.List;

/**
 * Created by hufan on 2017/1/13.
 */
public class VanChartGanttStylePane extends VanChartStylePane {
    public VanChartGanttStylePane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected void addOtherAxisPane(java.util.List<BasicPane> paneList, Plot plot) {
        addProjectAxisPane(paneList, plot);
        addTimeAxisPane(paneList, plot);
    }


    private void addTimeAxisPane(List<BasicPane> paneList, Plot plot) {
        paneList.add(new GanttTimeAxisPane());
    }

    private void addProjectAxisPane(List<BasicPane> paneList, Plot plot) {
        paneList.add(new GanttProcessAxisPane());
    }
}
