package com.fr.van.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.type.GaugeStyle;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.label.LabelContentPaneWithCate;
import com.fr.van.chart.designer.component.label.LabelContentPaneWithPercent;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * 仪表盘的分类（多指针时）或者百分比标签
 */
public class VanChartGaugeCateOrPercentLabelDetailPane extends VanChartGaugeLabelDetailPane {

    private static final long serialVersionUID = 5176535960949074945L;

    public VanChartGaugeCateOrPercentLabelDetailPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    protected double[] getLabelPaneRowSize(Plot plot, double p) {
        if (hasLabelAlign(plot)) {
            return new double[]{p, p, p, p};
        }

        if (hasLabelPosition(plot)) {
            return new double[]{p, p, p};
        }

        return new double[]{p, p};
    }

    protected void initToolTipContentPane(Plot plot) {
        switch (getGaugeStyle()) {
            case POINTER:
            case POINTER_SEMI:
                dataLabelContentPane = new LabelContentPaneWithCate(parent, VanChartGaugeCateOrPercentLabelDetailPane.this);
                break;
            default:
                dataLabelContentPane = new LabelContentPaneWithPercent(parent, VanChartGaugeCateOrPercentLabelDetailPane.this);
                break;
        }
    }

    protected boolean getFontSizeAuto() {
        return ComparatorUtils.equals(getGaugeStyle(), GaugeStyle.RING) || ComparatorUtils.equals(getGaugeStyle(), GaugeStyle.SLOT);
    }

    protected boolean hasLabelPosition(Plot plot) {
        switch (getGaugeStyle()) {
            case RING:
            case SLOT:
                return false;
            default:
                return true;
        }
    }

    protected double[] getLabelStyleRowSize(double p) {
        return new double[]{p, p};
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, Component component) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, component);
    }
}