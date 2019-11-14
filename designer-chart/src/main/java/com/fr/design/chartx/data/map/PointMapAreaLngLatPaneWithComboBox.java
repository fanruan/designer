package com.fr.design.chartx.data.map;

import com.fr.chartx.data.field.diff.PointMapColumnFieldCollection;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.i18n.Toolkit;

import javax.swing.JPanel;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/13
 */
public class PointMapAreaLngLatPaneWithComboBox extends PointMapAreaLngLatPaneWithTinyFormula {
    private UIComboBox area_tab0;

    private UIComboBox area_tab1;
    private UIComboBox lng_tab1;
    private UIComboBox lat_tab1;

    @Override
    protected JPanel createAreaPane() {
        if (area_tab0 == null) {
            area_tab0 = new UIComboBox();
        }
        return createPane(
                new String[]{Toolkit.i18nText("Fine-Design_Chart_Area_Name")},
                area_tab0
        );
    }

    @Override
    protected JPanel createAreaLngLatPane() {
        if (area_tab1 == null) {
            area_tab1 = new UIComboBox();
            lng_tab1 = new UIComboBox();
            lat_tab1 = new UIComboBox();
        }
        return createPane(
                new String[]{Toolkit.i18nText("Fine-Design_Chart_Area_Name"), Toolkit.i18nText("Fine-Design_Chart_Longitude"), Toolkit.i18nText("Fine-Design_Chart_Latitude")},
                area_tab1, lng_tab1, lat_tab1
        );
    }

    protected void populateTab0(PointMapColumnFieldCollection fieldCollection) {
        AbstractDataSetFieldsPane.populateField(area_tab0, fieldCollection.getAreaName());
    }

    protected void updateTab0(PointMapColumnFieldCollection fieldCollection) {
        AbstractDataSetFieldsPane.updateField(area_tab0, fieldCollection.getAreaName());
    }

    protected void populateTab1(PointMapColumnFieldCollection fieldCollection) {
        AbstractDataSetFieldsPane.populateField(area_tab1, fieldCollection.getAreaName());
        AbstractDataSetFieldsPane.populateField(lng_tab1, fieldCollection.getLng());
        AbstractDataSetFieldsPane.populateField(lat_tab1, fieldCollection.getLat());
    }

    protected void updateTab1(PointMapColumnFieldCollection fieldCollection) {
        AbstractDataSetFieldsPane.updateField(area_tab1, fieldCollection.getAreaName());
        AbstractDataSetFieldsPane.updateField(lng_tab1, fieldCollection.getLng());
        AbstractDataSetFieldsPane.updateField(lat_tab1, fieldCollection.getLat());
    }

    public UIComboBox[] allFieldComboBox() {
        return new UIComboBox[]{
                area_tab0, area_tab1, lng_tab1, lat_tab1
        };
    }
}
