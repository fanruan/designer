package com.fr.plugin.chart.area;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.plugin.chart.vanchart.AbstractMultiCategoryVanChartUI;

/**
 * Created by Mitisky on 15/11/18.
 */
public class AreaIndependentVanChartInterface extends AbstractMultiCategoryVanChartUI {
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/area.png";
    }

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartAreaPlotPane();
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartAreaConditionPane(plot);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartAreaSeriesPane(parent, plot);
    }

    /**
     * plot面板的标题
     * 插件兼容
     */
    public String getPlotTypeTitle4PopupWindow(){
        return VanChartAreaPlotPane.TITLE;
    }

}