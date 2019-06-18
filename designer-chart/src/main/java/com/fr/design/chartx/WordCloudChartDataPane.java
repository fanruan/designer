package com.fr.design.chartx;

import com.fr.design.chartx.fields.diff.WordCloudCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.WordCloudDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;

/**
 * Created by shine on 2019/5/22.
 */
public class WordCloudChartDataPane extends MultiCategoryChartDataPane {
    public WordCloudChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected SingleDataPane createSingleDataPane() {
        return new SingleDataPane(new WordCloudDataSetFieldsPane(), new WordCloudCellDataFieldsPane());
    }
}
