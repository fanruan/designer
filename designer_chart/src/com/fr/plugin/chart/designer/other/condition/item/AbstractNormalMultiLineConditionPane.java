package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Plot;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.plugin.chart.attr.plot.VanChartPlot;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 16/5/23.
 * 标签等有多行设置的条件属性,需要重新布局,使标签位于左上方
 */
public abstract class AbstractNormalMultiLineConditionPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {
    private VanChartPlot plot;

    public VanChartPlot getPlot() {
        return plot;
    }

    protected abstract String getItemLabelString();
    protected abstract JPanel initContentPane();

    public AbstractNormalMultiLineConditionPane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, null);
    }

    public AbstractNormalMultiLineConditionPane(ConditionAttributesPane conditionAttributesPane, Plot plot) {
        super(conditionAttributesPane, true);
        this.plot = (VanChartPlot) plot;
        UILabel nameLabel = new UILabel(getItemLabelString());

        JPanel pane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        this.removeAll();
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        // 重新布局
        JPanel northPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        northPane.setPreferredSize(new Dimension(100, 30));
        this.add(northPane, BorderLayout.NORTH);

        northPane.add(cancel);
        northPane.add(nameLabel);

        pane.setBorder(BorderFactory.createEmptyBorder(6, 50, 0, 300));

        pane.add(initContentPane());

        this.add(pane);
    }
}
