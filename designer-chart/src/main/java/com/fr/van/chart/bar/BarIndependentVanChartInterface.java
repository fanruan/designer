package com.fr.van.chart.bar;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.column.VanChartColumnConditionPane;
import com.fr.van.chart.column.VanChartColumnSeriesPane;
import com.fr.van.chart.designer.other.VanChartInteractivePane;
import com.fr.van.chart.designer.other.VanChartOtherPane;
import com.fr.van.chart.designer.other.zoom.ZoomPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.vanchart.AbstractMultiCategoryVanChartUI;

/**
 * Created by Mitisky on 15/10/20.
 */
public class BarIndependentVanChartInterface extends AbstractMultiCategoryVanChartUI {
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/bar.png";
    }

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartBarPlotPane();
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartColumnConditionPane(plot);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartColumnSeriesPane(parent, plot);
    }

    /**
     * 图表的属性界面数组
     * @return 属性界面
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        VanChartStylePane stylePane = new VanChartBarStylePane(listener);
        VanChartOtherPane otherPane = new VanChartOtherPane() {
            @Override
            protected BasicBeanPane<Chart> createInteractivePane() {
                return new VanChartInteractivePane() {
                    @Override
                    protected ZoomPane createZoomPane() {
                        return new ZoomPane();
                    }
                };
            }
        };
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

    /**
     * plot面板的标题
     * 插件兼容
     */
    public String getPlotTypeTitle4PopupWindow(){
        return VanChartBarPlotPane.TITLE;
    }

}