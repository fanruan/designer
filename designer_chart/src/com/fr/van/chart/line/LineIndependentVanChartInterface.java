package com.fr.van.chart.line;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.vanchart.AbstractMultiCategoryVanChartUI;

/**
 * Created by Mitisky on 15/11/5.
 */
public class LineIndependentVanChartInterface extends AbstractMultiCategoryVanChartUI {
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/line.png";
    }

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartLinePlotPane();
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartLineConditionPane(plot);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartLineSeriesPane(parent, plot);
    }

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartLinePlotPane.TITLE;
    }

}