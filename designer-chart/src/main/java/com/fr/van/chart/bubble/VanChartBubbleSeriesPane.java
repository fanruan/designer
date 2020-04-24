package com.fr.van.chart.bubble;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.plugin.chart.bubble.VanChartBubblePlot;
import com.fr.plugin.chart.bubble.attr.VanChartAttrBubble;
import com.fr.van.chart.bubble.component.VanChartBubblePane;
import com.fr.van.chart.custom.component.VanChartCustomAxisConditionPane;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.series.VanChartAbstractPlotSeriesPane;
import com.fr.van.chart.designer.style.series.VanChartStackedAndAxisListControlPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by Mitisky on 16/3/31.
 */
public class VanChartBubbleSeriesPane extends VanChartAbstractPlotSeriesPane {
    private static final long serialVersionUID = 5595016643808487932L;
    private VanChartBubblePane bubblePane;

    public VanChartBubbleSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p, p, p, p, p, p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{createBubblePane()},
                new Component[]{createStackedAndAxisPane()},
                new Component[]{null}
        };

        if (!((VanChartBubblePlot) plot).isForceBubble()) {
            components[2] = new Component[]{createLargeDataModelPane()};
        }

        contentPane = TableLayoutHelper.createTableLayoutPane(components, row, col);
        return contentPane;
    }

    //设置色彩面板内容
    @Override
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

    private JPanel createBubblePane() {
        bubblePane = new VanChartBubblePane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Bubble"), bubblePane);
    }

    protected void populateCondition(ConditionAttr defaultAttr) {
        super.populateCondition(defaultAttr);
        if (bubblePane != null) {
            VanChartAttrBubble attrBubble = defaultAttr.getExisted(VanChartAttrBubble.class);
            bubblePane.populateBean(attrBubble);
        }
    }

    protected void updateCondition(ConditionAttr defaultAttr) {
        super.updateCondition(defaultAttr);
        if (bubblePane != null) {
            VanChartAttrBubble attrBubble = defaultAttr.getExisted(VanChartAttrBubble.class);
            if (attrBubble != null) {
                defaultAttr.remove(attrBubble);
            }
            defaultAttr.addDataSeriesCondition(bubblePane.updateBean());
        }
    }
}
