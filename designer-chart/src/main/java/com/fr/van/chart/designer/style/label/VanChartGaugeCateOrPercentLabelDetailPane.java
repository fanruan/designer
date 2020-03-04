package com.fr.van.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.plugin.chart.type.GaugeStyle;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.label.LabelContentPaneWithCate;
import com.fr.van.chart.designer.component.label.LabelContentPaneWithPercent;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Component;

/**
 * 仪表盘的分类（多指针时）或者百分比标签
 */
public class VanChartGaugeCateOrPercentLabelDetailPane extends VanChartGaugeLabelDetailPane {
    //todo 重新整理这个面板

    private static final long serialVersionUID = 5176535960949074945L;

    public VanChartGaugeCateOrPercentLabelDetailPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    protected double[] getLabelPaneRowSize(Plot plot, double p) {
        return hasLabelPosition(plot) ? new double[]{p,p,p} : new double[]{p,p};
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

    protected double[] getLabelStyleRowSize(double p) {
        switch (gaugeStyle){
            case RING:
                return new double[] {p, p};
            case SLOT:
                return new double[] {p, p};
            default:
                return new double[] {p};
        }
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, Component component) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, component);
    }


    protected Component[][] getLabelStyleComponents(Plot plot) {
        initGaugeStyle(plot);
        if (gaugeStyle == GaugeStyle.RING || gaugeStyle == GaugeStyle.SLOT) {
            UILabel text = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Character"), SwingConstants.LEFT);
            return new Component[][]{
                    new Component[]{text,style},
                    new Component[]{textFontPane,null},
            };
        } else {
            return new Component[][]{
                    new Component[]{textFontPane, null},
            };
        }
    }

    protected ChartTextAttrPane initTextFontPane () {
        //todo 需要再整理下
        if (gaugeStyle == GaugeStyle.RING || gaugeStyle == GaugeStyle.SLOT){
            return new ChartTextAttrPane(){
                protected double[] getRowSize () {
                    double p = TableLayout.PREFERRED;
                    return new double[]{p, p};
                }

                protected Component[][] getComponents(JPanel buttonPane) {
                    return new Component[][]{
                            new Component[]{null, fontNameComboBox},
                            new Component[]{null, buttonPane}
                    };
                }
            };
        } else {
            return new ChartTextAttrPane(){
                protected double[] getRowSize () {
                    double p = TableLayout.PREFERRED;
                    return new double[]{p, p};
                }

                protected Component[][] getComponents(JPanel buttonPane) {
                    UILabel text = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Character"), SwingConstants.LEFT);
                    return new Component[][]{
                            new Component[]{text, fontNameComboBox},
                            new Component[]{null, buttonPane}
                    };
                }
            };
        }
    }
}