package com.fr.van.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.type.GaugeStyle;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.label.LabelContentPaneWithCateValue;
import com.fr.van.chart.designer.component.label.LabelContentPaneWithOutCate;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * 仪表盘的值标签
 */
public class VanChartGaugeValueLabelDetailPane extends VanChartGaugeLabelDetailPane {
    private static final long serialVersionUID = 2601073419430634281L;

    public VanChartGaugeValueLabelDetailPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    protected void initToolTipContentPane(Plot plot) {
        GaugeStyle gaugeStyle = ((VanChartGaugePlot) plot).getGaugeStyle();

        switch (gaugeStyle) {
            case POINTER:
            case POINTER_SEMI:
                dataLabelContentPane = new LabelContentPaneWithOutCate(parent, VanChartGaugeValueLabelDetailPane.this);
                break;
            default:
                dataLabelContentPane = new LabelContentPaneWithCateValue(parent, VanChartGaugeValueLabelDetailPane.this);
                break;
        }
    }

    protected Component[][] getLabelPaneComponents(Plot plot, double p, double[] columnSize) {
        GaugeStyle gaugeStyle = ((VanChartGaugePlot) plot).getGaugeStyle();

        switch (gaugeStyle) {
            case POINTER:
            case POINTER_SEMI:
                return getLabelPaneComponentsWithBackground(plot, p, columnSize);
            default:
                return super.getLabelPaneComponents(plot, p, columnSize);
        }
    }

    private Component[][] getLabelPaneComponentsWithBackground(Plot plot, double p, double[] columnSize) {
        return new Component[][]{
                new Component[]{dataLabelContentPane, null},
                new Component[]{createLabelStylePane(new double[]{p}, columnSize, plot), null},
                new Component[]{createBackgroundColorPane(), null},
        };
    }

    protected double[] getLabelPaneRowSize(Plot plot, double p) {
        GaugeStyle gaugeStyle = ((VanChartGaugePlot) plot).getGaugeStyle();

        switch (gaugeStyle) {
            case POINTER:
            case POINTER_SEMI:
                return new double[]{p, p, p, p, p};
            default:
                return super.getLabelPaneRowSize(plot, p);
        }
    }

    protected boolean getFontSizeAuto() {
        return ((VanChartGaugePlot) getPlot()).getGaugeStyle() != GaugeStyle.THERMOMETER;
    }

    protected boolean hasLabelPosition(Plot plot) {
        return ((VanChartGaugePlot) plot).getGaugeStyle() == GaugeStyle.THERMOMETER;
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, Component component) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, component);
    }
}