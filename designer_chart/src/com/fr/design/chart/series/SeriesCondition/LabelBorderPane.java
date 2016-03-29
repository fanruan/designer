package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.general.Inter;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class LabelBorderPane extends LabelBorderAttrPane {

    private AttrBorder attrBorder = new AttrBorder();

    public LabelBorderPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane, true, Inter.getLocText(new String[]{"Border", "Format"}));
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrBorder) {
            attrBorder = (AttrBorder) condition;
            linePane.setLineColor(attrBorder.getBorderColor());
            linePane.setLineStyle(attrBorder.getBorderStyle());
        }
    }

    public DataSeriesCondition update() {
        attrBorder.setBorderColor(linePane.getLineColor());
        attrBorder.setBorderStyle(linePane.getLineStyle());
        return attrBorder;
    }
}