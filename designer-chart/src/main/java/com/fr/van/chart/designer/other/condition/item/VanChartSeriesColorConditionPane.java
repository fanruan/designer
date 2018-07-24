package com.fr.van.chart.designer.other.condition.item;

import com.fr.base.background.ColorBackground;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.color.ColorSelectBox;


/**
 * 条件属性 配色（系列背景色）
 */
public class VanChartSeriesColorConditionPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {

    private static final long serialVersionUID = 1804818835947067586L;
    protected UILabel nameLabel;
    private ColorSelectBox colorSelectionPane;
    private AttrBackground attrBackground = new AttrBackground();

    public VanChartSeriesColorConditionPane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public VanChartSeriesColorConditionPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(conditionAttributesPane, isRemove);
        nameLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("plugin-ChartF_MatchColor"));
        colorSelectionPane = new ColorSelectBox(80);

        if (isRemove) {
            this.add(nameLabel);
        }
        this.add(colorSelectionPane);

    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("plugin-ChartF_MatchColor");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrBackground) {
            attrBackground = (AttrBackground) condition;
            ColorBackground background = (ColorBackground)attrBackground.getSeriesBackground();
            this.colorSelectionPane.setSelectObject(background.getColor());
        }
    }

    public DataSeriesCondition update() {
        ColorBackground colorBackground = ColorBackground.getInstance(this.colorSelectionPane.getSelectObject());
        attrBackground.setSeriesBackground(colorBackground);
        return attrBackground;
    }

}