package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.CoreConstants;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class LabelLineStylePane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {

    private UILabel nameLabel;
    private LineComboBox lineCombo;
    private AttrLineStyle attrLineStyle = new AttrLineStyle();

    public LabelLineStylePane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public LabelLineStylePane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(conditionAttributesPane, isRemove);


        nameLabel = new UILabel(Inter.getLocText("Line-Style") + ":");
        if (isRemove) {
            this.add(nameLabel);
        }

        lineCombo = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
        this.add(lineCombo);
    }

    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Line-Style");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    @Override
    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrLineStyle) {
            attrLineStyle = (AttrLineStyle) condition;
            int lineStyle = attrLineStyle.getLineStyle();
            if(lineStyle != Constants.LINE_THICK && lineStyle != Constants.LINE_THIN
                    && lineStyle != Constants.LINE_MEDIUM && lineStyle != Constants.LINE_NONE) {
                lineStyle = Constants.LINE_THIN;
            }
            lineCombo.setSelectedLineStyle(lineStyle);
        }
    }

    @Override
    public DataSeriesCondition update() {
        attrLineStyle.setLineStyle(lineCombo.getSelectedLineStyle());
        return attrLineStyle;
    }
}