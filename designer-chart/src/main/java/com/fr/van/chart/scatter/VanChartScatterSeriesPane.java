package com.fr.van.chart.scatter;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.van.chart.custom.component.VanChartCustomAxisConditionPane;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.VanChartLineTypePane;
import com.fr.van.chart.designer.style.series.VanChartAbstractPlotSeriesPane;
import com.fr.van.chart.designer.style.series.VanChartStackedAndAxisListControlPane;
import com.fr.van.chart.scatter.component.VanChartScatterLineTypePane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * 散点图的系列界面
 */
public class VanChartScatterSeriesPane extends VanChartAbstractPlotSeriesPane {
    private static final long serialVersionUID = 5595016643808487932L;

    public VanChartScatterSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {


        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p, p, p, p, p, p, p, p, p, p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{getColorPane()},
                new Component[]{createLineTypePane()},
                new Component[]{createMarkerPane()},
                new Component[]{createStackedAndAxisPane()},
                //大数据模式 恢复用注释。下面1行删除。
                new Component[]{createLargeDataModelPane()},
                new Component[]{createTrendLinePane()},
        };

        contentPane = TableLayoutHelper.createTableLayoutPane(components, row, col);
        return contentPane;
    }

    //设置色彩面板内容
    protected void setColorPaneContent(JPanel panel) {
        panel.add(createAlphaPane(), BorderLayout.CENTER);
    }

    @Override
    //堆积和坐标轴设置(自定义柱形图等用到)
    protected JPanel createStackedAndAxisPane() {
        stackAndAxisEditPane = new VanChartStackedAndAxisListControlPane() {
            @Override
            protected Class<? extends BasicBeanPane> getStackAndAxisPaneClass() {
                return VanChartCustomAxisConditionPane.class;
            }

            @Override
            public String getPaneTitle() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Axis");
            }
        };
        stackAndAxisEditExpandablePane = TableLayout4VanChartHelper.createExpandablePaneWithTitle(stackAndAxisEditPane.getPaneTitle(), stackAndAxisEditPane);
        return stackAndAxisEditExpandablePane;
    }

    @Override
    protected VanChartLineTypePane getLineTypePane() {
        return new VanChartScatterLineTypePane();
    }

    @Override
    protected void checkCompsEnabledWithLarge(Plot plot) {
        super.checkCompsEnabledWithLarge(this.plot);

        checkLinePane();
    }
}
