package com.fr.van.chart.multilayer;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.i18n.Toolkit;
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

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_New_MultiPie");
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/32.png"
        };
    }

//    @Override
//    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
//        return new AbstractDataPane(listener) {
//            @Override
//            protected SingleDataPane createSingleDataPane() {
//                return new SingleDataPane(new MultiPieDataSetFieldsPane(), new MultiPieCellDataFieldsPane());
//            }
//        };
//    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartMultiPieSeriesPane(parent, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartMultiPieConditionPane(plot);
    }

}
