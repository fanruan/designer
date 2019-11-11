package com.fr.design.chartx;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.plugin.chart.vanchart.VanChart;

import java.awt.BorderLayout;

/**
 * Created by shine on 2019/4/15.
 */
public abstract class AbstractChartDataPane<T extends ChartDataDefinitionProvider> extends ChartDataPane {

    public AbstractChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    protected abstract void populate(T t);

    protected abstract T update();

    @Override
    public void populate(ChartCollection collection) {
        if (collection == null) {
            return;
        }
        VanChart chart = collection.getSelectedChartProvider(VanChart.class);
        if (chart == null) {
            return;
        }

        this.removeAll();
        this.add(createContentPane(), BorderLayout.CENTER);

        ChartDataDefinitionProvider dataSetCollection = chart.getChartDataDefinition();

        populate((T) dataSetCollection);

        this.initAllListeners();
        this.validate();
    }


    @Override
    public void update(ChartCollection collection) {
        if (collection == null) {
            return;
        }
        VanChart chart = collection.getSelectedChartProvider(VanChart.class);
        if (chart == null) {
            return;
        }

        chart.setChartDataDefinition(update());
    }
}
