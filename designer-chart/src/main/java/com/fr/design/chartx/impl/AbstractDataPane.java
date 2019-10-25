package com.fr.design.chartx.impl;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by shine on 2019/09/04.
 */
public abstract class AbstractDataPane extends ChartDataPane {

    private SingleDataPane singleDataPane;

    public AbstractDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected JPanel createContentPane() {
        singleDataPane = createSingleDataPane();
        return singleDataPane;
    }

    protected abstract SingleDataPane createSingleDataPane();

    @Override
    public void populate(ChartCollection collection) {
        if (collection == null) {
            return;
        }
        VanChart chart = collection.getSelectedChart(VanChart.class);
        if (chart == null) {
            return;
        }

        this.removeAll();
        this.add(createContentPane(), BorderLayout.CENTER);

        ChartDataDefinitionProvider dataDefinition = chart.getChartDataDefinition();

        singleDataPane.populateBean((AbstractDataDefinition) dataDefinition);

        this.initAllListeners();
        this.validate();
    }


    @Override
    public void update(ChartCollection collection) {
        if (collection == null) {
            return;
        }
        VanChart chart = collection.getSelectedChart(VanChart.class);
        if (chart == null) {
            return;
        }

        chart.setChartDataDefinition(singleDataPane.updateBean());
    }
}
