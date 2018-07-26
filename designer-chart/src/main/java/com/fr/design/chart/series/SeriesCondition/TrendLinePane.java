package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrTrendLine;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class TrendLinePane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {

    private UILabel nameLabel;

    private UIButton editTrendLineButton;
    private AttrTrendLine attrTrendLine = new AttrTrendLine();

    public TrendLinePane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public TrendLinePane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(conditionAttributesPane, isRemove);
        nameLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Chart_TrendLine"));

        editTrendLineButton = new UIButton(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Edit", "Chart_TrendLine"}));
        editTrendLineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final TrendLineControlPane controlPane = new TrendLineControlPane();

                controlPane.populate(attrTrendLine);
                BasicDialog bg = controlPane.showWindow(SwingUtilities.getWindowAncestor(TrendLinePane.this),
                        new DialogActionAdapter() {
                            public void doOk() {
                                controlPane.update(attrTrendLine);
                            }
                        });

                bg.setVisible(true);
            }
        });
        if (isRemove) {
            this.add(nameLabel);
        }
        this.add(editTrendLineButton);
    }

    @Override
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Chart_TrendLine");
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrTrendLine) {
            attrTrendLine = (AttrTrendLine) condition;
        }
    }

    public DataSeriesCondition update() {

        return attrTrendLine;
    }
}