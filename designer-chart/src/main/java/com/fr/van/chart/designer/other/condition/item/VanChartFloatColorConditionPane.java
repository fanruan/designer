package com.fr.van.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.color.ColorSelectBox;

import com.fr.plugin.chart.base.AttrFloatColor;

/**
 * 条件属性 悬浮颜色
 */
public class VanChartFloatColorConditionPane extends ConditionAttrSingleConditionPane<DataSeriesCondition>{

    private static final long serialVersionUID = 1804818835947067586L;

    private ColorSelectBox colorSelectionPane;

    public VanChartFloatColorConditionPane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public VanChartFloatColorConditionPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(conditionAttributesPane, isRemove);
        UILabel nameLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Float_Color"));
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
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Float_Color");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrFloatColor) {
            AttrFloatColor attrColor = (AttrFloatColor) condition;
            this.colorSelectionPane.setSelectObject(attrColor.getSeriesColor());
        }
    }

    public DataSeriesCondition update() {
        AttrFloatColor attrColor = new AttrFloatColor();
        attrColor.setSeriesColor(this.colorSelectionPane.getSelectObject());
        return attrColor;
    }
}