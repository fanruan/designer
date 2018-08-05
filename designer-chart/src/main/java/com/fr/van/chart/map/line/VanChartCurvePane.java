package com.fr.van.chart.map.line;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.plugin.chart.map.line.condition.AttrCurve;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by hufan on 2016/12/19.
 */
public class VanChartCurvePane extends BasicBeanPane<AttrCurve>{
    private static final double MAX_WIDTH = 100;
    private static final double STEP = 0.5;
    private UISpinner lineWidth;
    private UINumberDragPane bending;
    private UINumberDragPane lineAlphaPane;

    public VanChartCurvePane() {
        initComponents();
    }

    private void initComponents(){
        lineWidth = new UISpinner(0, MAX_WIDTH, STEP, 0.5);
        bending = new UINumberDragPane(0, 100);
        lineAlphaPane = new UINumberDragPane(0, 100);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p, p, p, p};
        Component[][] components = getUseComponent();
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
    }

    private Component[][] getUseComponent() {
        return new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line_Width")), lineWidth},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Bedding")),bending},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alpha")), lineAlphaPane}
        };
    }

    @Override
    public void populateBean(AttrCurve attrCurve) {
        if (attrCurve == null){
            attrCurve = new AttrCurve();
        }
        lineWidth.setValue(attrCurve.getLineWidth());
        bending.populateBean(attrCurve.getBending());
        lineAlphaPane.populateBean(attrCurve.getAlpha());
    }

    public AttrCurve updateBean() {
        AttrCurve attrCurve = new AttrCurve();
        attrCurve.setBending(bending.updateBean());
        attrCurve.setLineWidth(lineWidth.getValue());
        attrCurve.setAlpha(lineAlphaPane.updateBean());
        return attrCurve;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
