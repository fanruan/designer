package com.fr.van.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrNode;
import com.fr.van.chart.structure.desinger.style.StructureNodeStylePane;

import javax.swing.JPanel;

/**
 * Created by shine on 2017/2/15.
 */
public class VanChartStructureNodeConditionPane extends AbstractNormalMultiLineConditionPane{
    private static final long serialVersionUID = 1924676751313839477L;
    private StructureNodeStylePane nodeStylePane;

    @Override
    protected String getItemLabelString() {
        return Inter.getLocText("Plugin-ChartF_Node");
    }

    @Override
    protected JPanel initContentPane() {
        nodeStylePane = new StructureNodeStylePane();
        return nodeStylePane;
    }

    public VanChartStructureNodeConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_Node");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }


    @Override
    public void setDefault() {
        populate(new AttrNode());
    }

    public void populate(DataSeriesCondition condition) {
        if(condition instanceof AttrNode){
            nodeStylePane.populateBean((AttrNode) condition);
        }

    }

    public DataSeriesCondition update() {
        return nodeStylePane.updateBean();
    }
}
