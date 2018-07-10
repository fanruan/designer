package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.ChartConstants;
import com.fr.design.condition.DSColumnLiteConditionPane;

public class ChartConditionPane extends DSColumnLiteConditionPane {

    public ChartConditionPane() {
        super();
        conditonTypePane.setVisible(false);

        populateColumns(columns2Populate());
    }

    public String[] columns2Populate() {
        return new String[]{
                ChartConstants.CATEGORY_INDEX,
                ChartConstants.CATEGORY_NAME,
                ChartConstants.SERIES_INDEX,
                ChartConstants.SERIES_NAME,
                ChartConstants.VALUE
        };
    }

}