package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.chart.comp.BorderAttriPane;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.stable.StringUtils;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public abstract class LabelBorderAttrPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {
    protected UILabel nameLabel;
    protected BorderAttriPane linePane;
    private String labelName;

    public LabelBorderAttrPane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, false, StringUtils.EMPTY);
    }

    public LabelBorderAttrPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove, String label) {
        super(conditionAttributesPane, isRemove);
        nameLabel = new UILabel(label);
        linePane = new BorderAttriPane();

        if (isRemove) {
            this.add(nameLabel);
        }
        this.add(linePane);
        this.labelName = label;
    }

    @Override
    public String nameForPopupMenuItem() {
        return labelName;
    }
}