package com.fr.design.chartx;

import com.fr.chartx.data.GanttChartDataDefinition;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.van.chart.map.designer.VanChartGroupPane;

import javax.swing.JPanel;

/**
 * Created by shine on 2019/5/22.
 */
public class GanttChartDataPane extends AbstractChartDataPane<GanttChartDataDefinition> {

    private MultiCategoryChartDataPane dataPane;
    private MultiCategoryChartDataPane linkPane;

    @Override
    protected JPanel createContentPane() {
        dataPane = new MultiCategoryChartDataPane(listener);
        linkPane = new MultiCategoryChartDataPane(listener);
        return new VanChartGroupPane(new String[]{"data", "link"}, new JPanel[]{dataPane, linkPane}) {
        };
    }

    public GanttChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected void populate(GanttChartDataDefinition ganttChartDataDefinition) {
        dataPane.populate(ganttChartDataDefinition.getDataDefinition());
        linkPane.populate(ganttChartDataDefinition.getLinkDefinition());
    }

    @Override
    protected GanttChartDataDefinition update() {
        return null;
    }
}
