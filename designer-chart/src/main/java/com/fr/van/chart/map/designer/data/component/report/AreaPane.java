package com.fr.van.chart.map.designer.data.component.report;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.van.chart.map.designer.data.component.LongitudeLatitudeAndArea;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by hufan on 2016/12/21.
 */
public class AreaPane extends AbstractReportDataContentPane {
    protected TinyFormulaPane areaName;

    public AreaPane() {
        JPanel panel = createContentPane();
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    protected JPanel createContentPane() {
        areaName = new TinyFormulaPane();
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, COMPONENT_WIDTH};
        double[] rowSize = {p};
        Component[][] components = getComponent();
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected Component[][] getComponent() {
        return new Component[][]{
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name")), areaName}
        };
    }

    @Override
    protected String[] columnNames() {
        return new String[0];
    }

    public void populate(LongitudeLatitudeAndArea longLatArea) {
        if (longLatArea.getArea() != null) {
            areaName.getUITextField().setText(longLatArea.getArea().toString());
        }
    }

    public LongitudeLatitudeAndArea update() {
        LongitudeLatitudeAndArea longLatArea = new LongitudeLatitudeAndArea();
        longLatArea.setArea(canBeFormula(areaName.getUITextField().getText()));
        return longLatArea;
    }

    @Override
    public void populateBean(ChartCollection ob) {

    }
}
