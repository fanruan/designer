package com.fr.design.chartx;

import com.fr.design.chartx.fields.diff.MultiCategoryCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.MultiCategoryDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;

/**
 * Created by shine on 2019/5/22.
 */
public class WordCloundChartDataPane extends MultiCategoryChartDataPane {
    public WordCloundChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected SingleDataPane createSingleDataPane() {
        return new SingleDataPane(new MultiCategoryDataSetFieldsPane(), new MultiCategoryCellDataFieldsPane());
    }
}
