package com.fr.van.chart.map.designer.data.component.report;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.van.chart.map.designer.data.component.LongitudeLatitudeAndArea;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by hufan on 2016/12/21.
 */
public class LongLatAreaPane extends AreaPane {
    protected TinyFormulaPane longitude;
    protected TinyFormulaPane latitude;

    public LongLatAreaPane() {
        JPanel panel = createContentPane();
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    @Override
    protected JPanel createContentPane() {
        areaName = new TinyFormulaPane();
        longitude = new TinyFormulaPane();
        latitude = new TinyFormulaPane();
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, COMPONENT_WIDTH};
        double[] rowSize = {p, p, p};
        Component[][] components = getComponent();
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 12, 6);
    }

    protected Component[][] getComponent() {
        return new Component[][]{
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Longitude")), longitude},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Latitude")), latitude},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name")), areaName}
        };
    }

    @Override
    public void populateBean(ChartCollection ob) {

    }

    @Override
    protected String[] columnNames() {
        return new String[0];
    }

    @Override
    public void populate(LongitudeLatitudeAndArea longLatArea) {
        super.populate(longLatArea);
        if (longLatArea.getLongitude() != null) {
            longitude.getUITextField().setText(longLatArea.getLongitude().toString());
        }
        if (longLatArea.getLatitude() != null) {
            latitude.getUITextField().setText(longLatArea.getLatitude().toString());
        }
    }

    @Override
    public LongitudeLatitudeAndArea update() {
        LongitudeLatitudeAndArea longLatArea = super.update();
        longLatArea.setLongitude(canBeFormula(longitude.getUITextField().getText()));
        longLatArea.setLatitude(canBeFormula(latitude.getUITextField().getText()));
        return longLatArea;
    }

}
