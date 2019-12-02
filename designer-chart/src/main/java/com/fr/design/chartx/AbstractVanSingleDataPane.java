package com.fr.design.chartx;

import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;

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
    protected JPanel createContentPane() {
        singleDataPane = createSingleDataPane();
        return singleDataPane;
    }

    protected abstract SingleDataPane createSingleDataPane();

    @Override
    protected void populate(AbstractDataDefinition dataDefinition) {
        singleDataPane.populateBean(dataDefinition);
    }

    @Override
    protected AbstractDataDefinition update() {
        return singleDataPane.updateBean();
    }

    public void setSelectedIndex(int index) {
        singleDataPane.setSelectedIndex(index);
    }
}
