package com.fr.design.chartx.data.map;

import com.fr.chartx.data.field.diff.LineMapColumnFieldCollection;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.i18n.Toolkit;

import javax.swing.JPanel;


/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/13
 */
public class LineMapAreaLngLatPaneWithTinyFormula extends AbstractAreaLngLatPane {
    private TinyFormulaPane fromArea_tab0;
    private TinyFormulaPane toArea_tab0;

    private TinyFormulaPane fromArea_tab1;
    private TinyFormulaPane fromLng_tab1;
    private TinyFormulaPane fromLat_tab1;
    private TinyFormulaPane toArea_tab1;
    private TinyFormulaPane toLng_tab1;
    private TinyFormulaPane toLat_tab1;


    @Override
    protected JPanel createAreaPane() {
        if (fromArea_tab0 == null) {
            fromArea_tab0 = new TinyFormulaPane();
            toArea_tab0 = new TinyFormulaPane();
        }
        return createPane(
                new String[]{Toolkit.i18nText("Fine-Design_Chart_Start_Area_Name"),
                        Toolkit.i18nText("Fine-Design_Chart_End_Area_Name")},
                fromArea_tab0, toArea_tab0
        );
    }

    @Override
    protected JPanel createAreaLngLatPane() {
        if (fromArea_tab1 == null) {
            fromArea_tab1 = new TinyFormulaPane();
            fromLng_tab1 = new TinyFormulaPane();
            fromLat_tab1 = new TinyFormulaPane();
            toArea_tab1 = new TinyFormulaPane();
            toLng_tab1 = new TinyFormulaPane();
            toLat_tab1 = new TinyFormulaPane();
        }
        return createPane(
                new String[]{
                        Toolkit.i18nText("Fine-Design_Chart_Start_Area_Name"),
                        Toolkit.i18nText("Fine-Design_Chart_Start_Longitude"),
                        Toolkit.i18nText("Fine-Design_Chart_Start_Latitude"),
                        Toolkit.i18nText("Fine-Design_Chart_End_Area_Name"),
                        Toolkit.i18nText("Fine-Design_Chart_End_Longitude"),
                        Toolkit.i18nText("Fine-Design_Chart_End_Latitude")},
                fromArea_tab1,
                fromLng_tab1,
                fromLat_tab1,
                toArea_tab1,
                toLng_tab1,
                toLat_tab1);
    }

    protected void populateTab0(LineMapColumnFieldCollection fieldCollection) {
        AbstractCellDataFieldsPane.populateField(fromArea_tab0, fieldCollection.getFromAreaName());
        AbstractCellDataFieldsPane.populateField(toArea_tab0, fieldCollection.getToAreaName());
    }

    protected void updateTab0(LineMapColumnFieldCollection fieldCollection) {
        AbstractCellDataFieldsPane.updateField(fromArea_tab0, fieldCollection.getFromAreaName());
        AbstractCellDataFieldsPane.updateField(toArea_tab0, fieldCollection.getToAreaName());
    }

    protected void populateTab1(LineMapColumnFieldCollection fieldCollection) {
        AbstractCellDataFieldsPane.populateField(fromArea_tab1, fieldCollection.getFromAreaName());
        AbstractCellDataFieldsPane.populateField(toArea_tab1, fieldCollection.getToAreaName());
        AbstractCellDataFieldsPane.populateField(fromLng_tab1, fieldCollection.getFromLng());
        AbstractCellDataFieldsPane.populateField(toLng_tab1, fieldCollection.getToLng());
        AbstractCellDataFieldsPane.populateField(fromLat_tab1, fieldCollection.getFromLat());
        AbstractCellDataFieldsPane.populateField(toLat_tab1, fieldCollection.getToLat());
    }

    protected void updateTab1(LineMapColumnFieldCollection fieldCollection) {
        AbstractCellDataFieldsPane.updateField(fromArea_tab1, fieldCollection.getFromAreaName());
        AbstractCellDataFieldsPane.updateField(toArea_tab1, fieldCollection.getToAreaName());
        AbstractCellDataFieldsPane.updateField(fromLng_tab1, fieldCollection.getFromLng());
        AbstractCellDataFieldsPane.updateField(toLng_tab1, fieldCollection.getToLng());
        AbstractCellDataFieldsPane.updateField(fromLat_tab1, fieldCollection.getFromLat());
        AbstractCellDataFieldsPane.updateField(toLat_tab1, fieldCollection.getToLat());
    }

    public void populate(LineMapColumnFieldCollection fieldCollection) {
        super.populate(fieldCollection.isUseAreaName());
        if (fieldCollection.isUseAreaName()) {
            populateTab0(fieldCollection);
        } else {
            populateTab1(fieldCollection);
        }
    }

    public void update(LineMapColumnFieldCollection fieldCollection) {
        fieldCollection.setUseAreaName(super.update());
        if (fieldCollection.isUseAreaName()) {
            updateTab0(fieldCollection);
        } else {
            updateTab1(fieldCollection);
        }
    }
}

