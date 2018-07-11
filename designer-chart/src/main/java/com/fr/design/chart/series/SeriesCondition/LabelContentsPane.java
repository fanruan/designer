package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrContents;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class LabelContentsPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {

    private UILabel nameLabel;
    private DataLabelContentsPane dataLabelContentsPane;

    private AttrContents attrContents = new AttrContents();

    public LabelContentsPane(ConditionAttributesPane conditionAttributesPane, Class plotClass) {
        this(conditionAttributesPane, true, plotClass);
    }

    public LabelContentsPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove, final Class plotClass) {
        super(conditionAttributesPane, isRemove);
        nameLabel = new UILabel(Inter.getLocText(new String[]{"Label", "HJS-Message"}));

        JPanel pane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        if (isRemove) {
            this.removeAll();
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            // 重新布局
            JPanel northPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            northPane.setPreferredSize(new Dimension(300, 30));
            this.add(northPane, BorderLayout.NORTH);

            northPane.add(cancel);
            northPane.add(nameLabel);

            pane.setBorder(BorderFactory.createEmptyBorder(6, 25, 0, 0));
        }

        this.dataLabelContentsPane = new DataLabelContentsPane(plotClass);

        pane.add(dataLabelContentsPane);

        this.add(pane);
    }

    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText(new String[]{"Label", "HJS-Message"});
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    /**
     * 检查是否使用牵引线box.
     */
    public void checkGuidBox() {
        if (this.dataLabelContentsPane != null) {
            this.dataLabelContentsPane.checkGuidBox();
        }
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrContents) {
            attrContents = (AttrContents) condition;
            this.dataLabelContentsPane.populate(attrContents);
        }
    }

    public DataSeriesCondition update() {
        this.dataLabelContentsPane.update(attrContents);
        return attrContents;
    }
}