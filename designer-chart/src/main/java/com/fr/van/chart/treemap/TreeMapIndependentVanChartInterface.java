package com.fr.van.chart.treemap;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.designer.other.VanChartInteractivePaneWithOutSort;
import com.fr.van.chart.designer.other.VanChartOtherPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.multilayer.MultiPieIndependentVanChartInterface;
import com.fr.van.chart.treemap.style.VanChartTreeMapSeriesPane;

/**
 * Created by Fangjie on 2016/7/11.
 */
public class TreeMapIndependentVanChartInterface extends MultiPieIndependentVanChartInterface {
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartTreeMapPlotPane();
    }

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_New_TreeMap");
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/40.png"
        };
    }

    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/treeMap.png";
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartTreeMapSeriesPane(parent, plot);
    }

    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        VanChartStylePane stylePane = new VanChartStylePane(listener);
        VanChartOtherPane otherPane = new VanChartOtherPane(){
            @Override
            protected BasicBeanPane<Chart> createInteractivePane() {
                return new VanChartInteractivePaneWithOutSort();
            }

        };
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

}
