package com.fr.plugin.chart.map.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.designer.other.condition.item.AbstractNormalMultiLineConditionPane;
import com.fr.plugin.chart.map.designer.style.series.VanChartMapScatterMarkerPane;

import javax.swing.*;

/**
 * Created by Mitisky on 16/5/23.
 */
public class VanChartCommonMarkerConditionPane extends AbstractNormalMultiLineConditionPane {
    private VanChartMapScatterMarkerPane commonMarkerPane;

    public VanChartCommonMarkerConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    @Override
    protected String getItemLabelString() {
        return Inter.getLocText("Plugin-ChartF_Marker");
    }

    @Override
    protected JPanel initContentPane() {
        commonMarkerPane = new VanChartMapScatterMarkerPane();
        return commonMarkerPane;
    }

    /**
     * 条目名称
     *
     * @return 名称
     */
    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_Marker");
    }

    @Override
    public void populate(DataSeriesCondition condition) {
        if(condition instanceof VanChartAttrMarker){
            commonMarkerPane.populateBean((VanChartAttrMarker)condition);
        }

    }

    @Override
    public DataSeriesCondition update() {
        return commonMarkerPane.updateBean();
    }
}
