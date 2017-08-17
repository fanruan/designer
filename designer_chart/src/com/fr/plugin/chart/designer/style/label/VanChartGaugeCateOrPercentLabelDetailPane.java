package com.fr.plugin.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.label.LabelContentPaneWithCate;
import com.fr.plugin.chart.designer.component.label.LabelContentPaneWithPercent;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.type.GaugeStyle;

import javax.swing.*;
import java.awt.*;

/**
 * 仪表盘的分类（多指针时）或者百分比标签
 */
public class VanChartGaugeCateOrPercentLabelDetailPane extends VanChartGaugeLabelDetailPane {

    private static final long serialVersionUID = 5176535960949074945L;

    private GaugeStyle gaugeStyle;

    public VanChartGaugeCateOrPercentLabelDetailPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    private void initGaugeStyle(Plot plot) {
        if(gaugeStyle == null){
            gaugeStyle = ((VanChartGaugePlot)plot).getGaugeStyle();
        }
    }

    protected void initToolTipContentPane(Plot plot) {
        initGaugeStyle(plot);
        switch (gaugeStyle){
            case POINTER:
                dataLabelContentPane = new LabelContentPaneWithCate(parent, VanChartGaugeCateOrPercentLabelDetailPane.this);
                break;
            case POINTER_SEMI:
                dataLabelContentPane = new LabelContentPaneWithCate(parent, VanChartGaugeCateOrPercentLabelDetailPane.this);
                break;
            default:
                dataLabelContentPane = new LabelContentPaneWithPercent(parent, VanChartGaugeCateOrPercentLabelDetailPane.this);
                break;
        }
    }

    protected boolean hasLabelPosition(Plot plot) {
        initGaugeStyle(plot);
        switch (gaugeStyle){
            case RING:
                return false;
            case SLOT:
                return false;
            default:
                return true;
        }
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, Component component) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, component);
    }

    protected Component[][] getLabelStyleComponents(Plot plot) {
        initGaugeStyle(plot);
        switch (gaugeStyle){
            case RING:
                return super.getLabelStyleComponents(plot);
            case SLOT:
                return super.getLabelStyleComponents(plot);
            default:
                return new Component[][]{
                        new Component[]{textFontPane,null},
                };
        }
    }
}