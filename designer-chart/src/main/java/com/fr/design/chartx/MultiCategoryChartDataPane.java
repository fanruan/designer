package com.fr.design.chartx;

import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.design.chartx.fields.diff.MultiCategoryCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.MultiCategoryDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;

import javax.swing.JPanel;

/**
 * Created by shine on 2019/5/22.
 */
public class MultiCategoryChartDataPane extends AbstractChartDataPane<AbstractDataDefinition> {

    private SingleDataPane singleDataPane;

    public MultiCategoryChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected void populate(AbstractDataDefinition dataDefinition) {
        singleDataPane.populateBean(dataDefinition);
    }

    @Override
    protected AbstractDataDefinition update() {
        return singleDataPane.updateBean();
    }

    @Override
    protected JPanel createContentPane() {
        singleDataPane = createSingleDataPane();
        return singleDataPane;
    }

    protected SingleDataPane createSingleDataPane() {
        return new SingleDataPane(new MultiCategoryDataSetFieldsPane(), new MultiCategoryCellDataFieldsPane());
    }


}
