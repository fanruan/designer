package com.fr.van.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundQuickPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrSeriesImageBackground;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 15/10/20.
 */
public class VanChartSeriesImageBackgroundConditionPane extends AbstractNormalMultiLineConditionPane {

    private static final long serialVersionUID = 1804818835947067586L;
    protected UILabel nameLabel;
    private ImageBackgroundQuickPane imageBackgroundPane;
    private AttrSeriesImageBackground attrBackground = new AttrSeriesImageBackground();

    @Override
    protected String getItemLabelString() {
        return Inter.getLocText("Plugin-ChartF_FilledWithImage");
    }

    @Override
    protected JPanel initContentPane() {
        imageBackgroundPane = new ImageBackgroundQuickPane(false);
        return imageBackgroundPane;
    }

    public VanChartSeriesImageBackgroundConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_FilledWithImage");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrSeriesImageBackground) {
            attrBackground = (AttrSeriesImageBackground) condition;
            this.imageBackgroundPane.populateBean(attrBackground.getSeriesBackground());
        }
    }

    public DataSeriesCondition update() {
        com.fr.general.Background imageBackground = imageBackgroundPane.updateBean();
        attrBackground.setSeriesBackground(imageBackground);
        return attrBackground;
    }
}