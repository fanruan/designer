package com.fr.design.chartx;

import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.JPanel;

/**
 * Created by shine on 2019/09/06.
 */
public abstract class AbstractVanSingleDataPane extends AbstractChartDataPane<AbstractDataDefinition> {
    private SingleDataPane singleDataPane;


    public AbstractVanSingleDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected JPanel createContentPane(VanChart vanChart) {
        singleDataPane = createSingleDataPane(vanChart);
        return singleDataPane;
    }

    protected abstract SingleDataPane createSingleDataPane(VanChart vanChart);

    @Override
    protected void populate(AbstractDataDefinition dataDefinition) {
        singleDataPane.populateBean(dataDefinition);
    }

    @Override
    protected AbstractDataDefinition update() {
        return singleDataPane.updateBean();
    }
}
