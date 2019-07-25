package com.fr.design.chartx;

import com.fr.design.chartx.fields.diff.MultiPieCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.MultiPieDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;

/**
 * Created by shine on 2019/6/18.
 */
public class MultiPieChartDataPane extends MultiCategoryChartDataPane {

    public MultiPieChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected SingleDataPane createSingleDataPane() {
        return new SingleDataPane(new MultiPieDataSetFieldsPane(), new MultiPieCellDataFieldsPane());
    }
}
