package com.fr.design.chartx.data.map;

import com.fr.chartx.data.field.diff.PointMapColumnFieldCollection;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.i18n.Toolkit;

import javax.swing.JPanel;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/13
 */
public class PointMapAreaLngLatPaneWithTinyFormula extends AbstractAreaLngLatPane {
    private TinyFormulaPane area_tab0;

    private TinyFormulaPane area_tab1;
    private TinyFormulaPane lng_tab1;
    private TinyFormulaPane lat_tab1;


    @Override
    protected JPanel createAreaPane() {
        if (area_tab0 == null) {
            area_tab0 = new TinyFormulaPane();
        }
        return createPane(
                new String[]{Toolkit.i18nText("Fine-Design_Chart_Area_Name")},
                area_tab0
        );
    }

    @Override
    protected JPanel createAreaLngLatPane() {
        if (area_tab1 == null) {
            area_tab1 = new TinyFormulaPane();
            lng_tab1 = new TinyFormulaPane();
            lat_tab1 = new TinyFormulaPane();
        }
        return createPane(
                new String[]{Toolkit.i18nText("Fine-Design_Chart_Area_Name"), Toolkit.i18nText("Fine-Design_Chart_Longitude"), Toolkit.i18nText("Fine-Design_Chart_Latitude")},
                area_tab1, lng_tab1, lat_tab1
        );
    }

    protected void populateTab0(PointMapColumnFieldCollection fieldCollection) {
        AbstractCellDataFieldsPane.populateField(area_tab0, fieldCollection.getAreaName());
    }

    protected void updateTab0(PointMapColumnFieldCollection fieldCollection) {
        AbstractCellDataFieldsPane.updateField(area_tab0, fieldCollection.getAreaName());
    }

    protected void populateTab1(PointMapColumnFieldCollection fieldCollection) {
        AbstractCellDataFieldsPane.populateField(area_tab1, fieldCollection.getAreaName());
        AbstractCellDataFieldsPane.populateField(lng_tab1, fieldCollection.getLng());
        AbstractCellDataFieldsPane.populateField(lat_tab1, fieldCollection.getLat());
    }

    protected void updateTab1(PointMapColumnFieldCollection fieldCollection) {
        AbstractCellDataFieldsPane.updateField(area_tab1, fieldCollection.getAreaName());
        AbstractCellDataFieldsPane.updateField(lng_tab1, fieldCollection.getLng());
        AbstractCellDataFieldsPane.updateField(lat_tab1, fieldCollection.getLat());
    }

    public void populate(PointMapColumnFieldCollection fieldCollection) {
        super.populate(fieldCollection.isUseAreaName());
        if (fieldCollection.isUseAreaName()) {
            populateTab0(fieldCollection);
        } else {
            populateTab1(fieldCollection);
        }
    }

    public void update(PointMapColumnFieldCollection fieldCollection) {
        fieldCollection.setUseAreaName(super.update());
        if (fieldCollection.isUseAreaName()) {
            updateTab0(fieldCollection);
        } else {
            updateTab1(fieldCollection);
        }
    }
}
