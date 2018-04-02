package com.fr.van.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Plot;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.van.chart.designer.style.label.VanChartPlotLabelNoCheckPane;
import com.fr.van.chart.designer.style.label.VanChartPlotLabelPane;

import javax.swing.JPanel;

public class VanChartLabelConditionPane extends AbstractNormalMultiLineConditionPane {

    private static final long serialVersionUID = 1338868748575437659L;
    protected VanChartPlotLabelPane dataLabelContentsPane;

    @Override
    protected String getItemLabelString() {
        return nameForPopupMenuItem();
    }

    @Override
    protected JPanel initContentPane() {
        dataLabelContentsPane = createLabelPane();
        return dataLabelContentsPane;
    }

    protected VanChartPlotLabelPane createLabelPane() {
        return new VanChartPlotLabelNoCheckPane(getPlot(),null);
    }

    public VanChartLabelConditionPane(ConditionAttributesPane conditionAttributesPane, Plot plot) {
        super(conditionAttributesPane, plot);
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("FR-Chart-Chart_Label");
    }

    public void setDefault() {
        //下面这句话是给各组件一个默认值
        this.dataLabelContentsPane.populate(getPlot().getDefaultAttrLabel());
    }


    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrLabel) {
            this.dataLabelContentsPane.populate((AttrLabel) condition);
        }
    }

    public DataSeriesCondition update() {
        return this.dataLabelContentsPane.update();
    }
}