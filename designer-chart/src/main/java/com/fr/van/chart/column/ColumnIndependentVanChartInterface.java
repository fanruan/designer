package com.fr.van.chart.column;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chartx.MultiCategoryChartDataPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.vanchart.AbstractMultiCategoryVanChartUI;

/**
 * Created by Mitisky on 15/9/24.
 */
public class ColumnIndependentVanChartInterface extends AbstractMultiCategoryVanChartUI {
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/column.png";
    }

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartColumnPlotPane();
    }


    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartColumnConditionPane(plot);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartColumnSeriesPane(parent, plot);
    }

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartColumnPlotPane.TITLE;
    }

    @Override
    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
        return new MultiCategoryChartDataPane(listener);
    }
}