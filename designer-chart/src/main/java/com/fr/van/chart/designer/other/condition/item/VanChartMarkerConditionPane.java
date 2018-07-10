package com.fr.van.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.van.chart.designer.component.VanChartMarkerPane;

import javax.swing.JPanel;

/**
 * 标记点条件属性界面
 */
public class VanChartMarkerConditionPane extends AbstractNormalMultiLineConditionPane{
    private static final long serialVersionUID = -4148284851967140012L;
    protected VanChartMarkerPane markerPane;

    @Override
    protected String getItemLabelString() {
        return Inter.getLocText("Plugin-ChartF_Marker");
    }

    @Override
    protected JPanel initContentPane() {
        initMarkerPane();
        return markerPane;
    }

    public VanChartMarkerConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    protected void initMarkerPane() {
        markerPane = new VanChartMarkerPane();
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_Marker");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void setDefault() {
        //下面这句话是给各组件一个默认值
        this.markerPane.populate(new VanChartAttrMarker());
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof VanChartAttrMarker) {
            markerPane.populate((VanChartAttrMarker) condition);
        }
    }

    public DataSeriesCondition update() {
        return markerPane.update();
    }
}