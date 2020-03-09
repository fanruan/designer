package com.fr.van.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;

import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.type.GaugeStyle;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.design.i18n.Toolkit;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by Mitisky on 16/3/1.
 */
public class VanChartGaugePlotLabelPane extends VanChartPlotLabelPane {
    private static final long serialVersionUID = -322148616244458359L;

    private VanChartPlotLabelDetailPane gaugeValueLabelPane;

    public VanChartGaugePlotLabelPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    protected void createLabelPane() {
        labelPane = new JPanel(new BorderLayout(0, 4));
        labelDetailPane = new VanChartGaugeCateOrPercentLabelDetailPane(this.plot, this.parent);
        gaugeValueLabelPane = new VanChartGaugeValueLabelDetailPane(this.plot, this.parent);
        GaugeStyle gaugeStyle = ((VanChartGaugePlot)this.plot).getGaugeStyle();
        String cateTitle, valueTitle = Toolkit.i18nText("Fine-Design_Chart_Value_Label");
        switch (gaugeStyle){
            case POINTER:
            case POINTER_SEMI:
                cateTitle = Toolkit.i18nText("Fine-Design_Chart_Category_Label");
                break;
            default:
                cateTitle = Toolkit.i18nText("Fine-Design_Chart_Percent_Label");
                break;
        }
        JPanel cateOrPercentPane = TableLayout4VanChartHelper.createExpandablePaneWithTitle(cateTitle, labelDetailPane);
        JPanel valuePane = TableLayout4VanChartHelper.createExpandablePaneWithTitle(valueTitle, gaugeValueLabelPane);
        labelPane.add(cateOrPercentPane, BorderLayout.NORTH);
        labelPane.add(valuePane, BorderLayout.SOUTH);
    }

    public void populate(AttrLabel attr) {
        super.populate(attr);

        if(gaugeValueLabelPane != null && attr != null){
            gaugeValueLabelPane.populate(attr.getGaugeValueLabelDetail());
        }
    }

    public AttrLabel update() {
        AttrLabel attrLabel = super.update();
        if(gaugeValueLabelPane != null && attrLabel != null){
            gaugeValueLabelPane.update(attrLabel.getGaugeValueLabelDetail());
        }
        return attrLabel;
    }
}
