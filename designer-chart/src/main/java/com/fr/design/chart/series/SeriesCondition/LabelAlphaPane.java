package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.UILabel;


import javax.swing.*;
import java.awt.*;

/**
 * @author richie
 * @date 2015-03-26
 * @since 8.0
 */
public class LabelAlphaPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {
    private static final double ALPHASIZE = 100.0;
    private static final int PANEL_WIDTH = 200;
    private static final int PANEL_HIGHT = 20;

    private UILabel nameLabel;
    private UINumberDragPane alphaPane;


    private AttrAlpha attrAlpha = new AttrAlpha();

    public LabelAlphaPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane, true);

        nameLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alpha"));
        UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alpha") + ":");
        alphaPane = new UINumberDragPane(0, ALPHASIZE);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(label, BorderLayout.WEST);
        panel.add(alphaPane, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HIGHT));

        this.add(nameLabel);
        this.add(panel);
    }

    @Override
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alpha");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrAlpha) {
            attrAlpha = (AttrAlpha) condition;
            alphaPane.populateBean(attrAlpha.getAlpha() * ALPHASIZE);
        }
    }

    public DataSeriesCondition update() {
        attrAlpha.setAlpha((float) (this.alphaPane.updateBean() / ALPHASIZE));

        return attrAlpha;
    }
}