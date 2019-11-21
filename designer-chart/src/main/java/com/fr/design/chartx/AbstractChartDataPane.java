package com.fr.design.chartx;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.stable.AssistUtils;

import java.awt.BorderLayout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by shine on 2019/4/15.
 */
public abstract class AbstractChartDataPane<T extends ChartDataDefinitionProvider> extends ChartDataPane {

    private VanChart vanChart;

    protected VanChart getVanChart() {
        return vanChart;
    }

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
        vanChart = collection.getSelectedChartProvider(VanChart.class);
        if (vanChart == null) {
            return;
        }

        this.removeAll();
        this.add(createContentPane(), BorderLayout.CENTER);

        ChartDataDefinitionProvider dataSetCollection = vanChart.getChartDataDefinition();

        if (isMatchedDataType(dataSetCollection)) {
            populate((T) dataSetCollection);
        }

        this.initAllListeners();
        this.validate();
    }

    private boolean isMatchedDataType(ChartDataDefinitionProvider dataDefinition) {
        if (dataDefinition == null) {
            return true;
        }

        Type dataType = this.getClass().getGenericSuperclass();
        if (dataType instanceof ParameterizedType) {
            dataType = ((ParameterizedType) dataType).getActualTypeArguments()[0];
            return AssistUtils.equals(dataDefinition.getClass(), dataType);
        } else if (dataType instanceof Class) {
            dataType = ((ParameterizedType) (((Class) dataType).getGenericSuperclass())).getActualTypeArguments()[0];
            return ((Class) dataType).isAssignableFrom(dataDefinition.getClass());
        }

        return true;
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
