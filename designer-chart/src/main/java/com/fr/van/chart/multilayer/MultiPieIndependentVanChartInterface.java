package com.fr.van.chart.multilayer;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chartx.MultiPieChartDataPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.multilayer.other.VanChartMultiPieConditionPane;
import com.fr.van.chart.multilayer.style.VanChartMultiPieSeriesPane;
import com.fr.van.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by Fangjie on 2016/6/15.
 */
public class MultiPieIndependentVanChartInterface extends AbstractIndependentVanChartUI {
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartMultiPiePlotPane();
    }

    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/multiPie.png";
    }

//    @Override
//    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
//        return new MultiPieChartDataPane(listener);
//    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartMultiPieSeriesPane(parent, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartMultiPieConditionPane(plot);
    }

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartMultiPiePlotPane.TITLE;
    }
}
