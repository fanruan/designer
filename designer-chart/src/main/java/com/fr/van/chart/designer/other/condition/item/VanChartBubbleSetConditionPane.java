package com.fr.van.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;

import com.fr.plugin.chart.bubble.attr.VanChartAttrBubble;
import com.fr.van.chart.bubble.component.VanChartBubblePane;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/3/31.
 */
public class VanChartBubbleSetConditionPane extends AbstractNormalMultiLineConditionPane {
    private static final long serialVersionUID = 1804818835947067586L;

    private VanChartBubblePane bubblePane;

    @Override
    protected String getItemLabelString() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Bubble");
    }

    @Override
    protected JPanel initContentPane() {
        bubblePane = new VanChartBubblePane();
        return bubblePane;
    }

    public VanChartBubbleSetConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    /**
     *  条目名称
     * @return 名称
     */
    @Override
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Bubble");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void setDefault() {
        //下面这句话是给各个组件一个默认值
        this.bubblePane.populateBean(new VanChartAttrBubble());
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof VanChartAttrBubble) {
            VanChartAttrBubble attrBubble = (VanChartAttrBubble) condition;
            this.bubblePane.populateBean(attrBubble);
        }
    }

    public DataSeriesCondition update() {
        return this.bubblePane.updateBean();
    }
}
