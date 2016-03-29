package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.AlphaPane;
import com.fr.general.Inter;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class LabelAlphaPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {
    private static final int ALPHASIZE = 100;

    private UILabel nameLabel;
    private AlphaPane alphaPane;

    private AttrAlpha attrAlpha = new AttrAlpha();

    public LabelAlphaPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane, true);

        nameLabel = new UILabel(Inter.getLocText("ChartF-Alpha"));
        alphaPane = new AlphaPane();

        this.add(nameLabel);
        this.add(alphaPane);
    }

    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("ChartF-Alpha");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrAlpha) {
            attrAlpha = (AttrAlpha) condition;
            alphaPane.populate((int) (attrAlpha.getAlpha() * ALPHASIZE));
        }
    }

    public DataSeriesCondition update() {
        attrAlpha.setAlpha(this.alphaPane.update());

        return attrAlpha;
    }
}