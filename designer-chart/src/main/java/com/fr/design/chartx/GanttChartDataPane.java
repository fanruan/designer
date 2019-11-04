package com.fr.design.chartx;

import com.fr.chartx.data.GanttChartDataDefinition;
import com.fr.design.chartx.fields.diff.MultiCategoryCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.MultiCategoryDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.map.designer.VanChartGroupPane;

import javax.swing.JPanel;

/**
 * Created by shine on 2019/5/22.
 */
public class GanttChartDataPane extends AbstractChartDataPane<GanttChartDataDefinition> {

    private AbstractVanSingleDataPane dataPane;
    private AbstractVanSingleDataPane linkPane;

    @Override
    protected JPanel createContentPane() {
        dataPane = new AbstractVanSingleDataPane(listener) {
            @Override
            protected SingleDataPane createSingleDataPane(VanChart vanChart) {
                return new SingleDataPane(new MultiCategoryDataSetFieldsPane(), new MultiCategoryCellDataFieldsPane());
            }
        };
        linkPane = new AbstractVanSingleDataPane(listener) {
            @Override
            protected SingleDataPane createSingleDataPane(VanChart vanChart) {
                return new SingleDataPane(new MultiCategoryDataSetFieldsPane(), new MultiCategoryCellDataFieldsPane());
            }
        };
        return new VanChartGroupPane(new String[]{"data", "link"}, new JPanel[]{dataPane, linkPane}) {
        };
    }

    public GanttChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    public void populate(GanttChartDataDefinition ganttChartDataDefinition) {
        dataPane.populate(ganttChartDataDefinition.getDataDefinition());
        linkPane.populate(ganttChartDataDefinition.getLinkDefinition());
    }

    @Override
    public GanttChartDataDefinition update() {
        return null;
    }
}
