package com.fr.plugin.chart.map.designer.data.component.report;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.map.designer.data.component.LongitudeLatitudeAndArea;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hufan on 2016/12/21.
 */
public class LongLatAreaPane extends AreaPane {
    private TinyFormulaPane longitude;
    private TinyFormulaPane latitude;

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
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Plugin-ChartF_Longitude") + ":", SwingConstants.RIGHT), longitude},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Plugin-ChartF_Latitude") + ":", SwingConstants.RIGHT), latitude},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Area_Name") + ":", SwingConstants.RIGHT), areaName}
        };
        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
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
        if (longLatArea.getLatitude() != null){
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
