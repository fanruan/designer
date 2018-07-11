package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class LabelColorPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {

    protected UILabel nameLabel;
    private ColorSelectBox colorSelectionPane;
    private AttrColor attrColor = new AttrColor();

    public LabelColorPane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public LabelColorPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(conditionAttributesPane, isRemove);
        nameLabel = new UILabel(Inter.getLocText("ChartF-Background_Color"));
        colorSelectionPane = new ColorSelectBox(80);

        if (isRemove) {
            this.add(nameLabel);
        }
        this.add(colorSelectionPane);

    }

    /**
     *  条目名称
     * @return 名称
     */
    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Color");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrColor) {
            attrColor = (AttrColor) condition;
            this.colorSelectionPane.setSelectObject(this.attrColor.getSeriesColor());
        }
    }

    public DataSeriesCondition update() {
        attrColor.setSeriesColor(this.colorSelectionPane.getSelectObject());
        return attrColor;
    }
}