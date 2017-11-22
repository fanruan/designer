package com.fr.plugin.chart.scatter;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import com.fr.plugin.chart.custom.component.VanChartCustomAxisConditionPane;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartLineTypePane;
import com.fr.plugin.chart.designer.style.series.VanChartAbstractPlotSeriesPane;
import com.fr.plugin.chart.designer.style.series.VanChartStackedAndAxisListControlPane;
import com.fr.plugin.chart.scatter.component.VanChartScatterLineTypePane;

import javax.swing.*;
import java.awt.*;

/**
 * 散点图的系列界面
 */
public class VanChartScatterSeriesPane extends VanChartAbstractPlotSeriesPane{
    private static final long serialVersionUID = 5595016643808487932L;

    public VanChartScatterSeriesPane(ChartStylePane parent, Plot plot){
        super(parent, plot);
    }

    protected JPanel getContentInPlotType(){


        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p,p,p,p,p,p,p,p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{getColorPane()},
                new Component[]{createLineTypePane()},
                new Component[]{createMarkerPane()},
                new Component[]{createStackedAndAxisPane()},
                new Component[]{createLargeDataModelPane()},
                new Component[]{createTrendLinePane()},
        };

        contentPane = TableLayoutHelper.createTableLayoutPane(components, row, col);
        return contentPane;
    }

    //设置色彩面板内容
    protected void setColorPaneContent (JPanel panel) {
        panel.add(createAlphaPane(), BorderLayout.CENTER);
    }

    @Override
    //堆积和坐标轴设置(自定义柱形图等用到)
    protected JPanel createStackedAndAxisPane() {
        stackAndAxisEditPane = new VanChartStackedAndAxisListControlPane(){
            @Override
            protected Class<? extends BasicBeanPane> getStackAndAxisPaneClass() {
                return VanChartCustomAxisConditionPane.class;
            }

            @Override
            public String getPaneTitle(){
                return Inter.getLocText("Plugin-ChartF_Custom_Axis");
            }
        };
        stackAndAxisEditExpandablePane =  TableLayout4VanChartHelper.createExpandablePaneWithTitle(stackAndAxisEditPane.getPaneTitle(), stackAndAxisEditPane);
        return stackAndAxisEditExpandablePane;
    }

    @Override
    protected VanChartLineTypePane getLineTypePane() {
        return new VanChartScatterLineTypePane();
    }

    @Override
    protected void checkCompsEnabledWithLarge() {
        super.checkCompsEnabledWithLarge();

        checkLinePane();
    }
}
