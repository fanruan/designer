package com.fr.design.chartx.data.map;

import com.fr.chartx.data.field.diff.LineMapColumnFieldCollection;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;
import com.fr.design.chartx.fields.diff.LineMapDataSetFieldsPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.i18n.Toolkit;

import javax.swing.JPanel;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/13
 */
public class LineMapAreaLngLatPaneWithComboBox extends LineMapAreaLngLatPaneWithTinyFormula {
    private UIComboBox fromArea_tab0;
    private UIComboBox toArea_tab0;

    private UIComboBox fromArea_tab1;
    private UIComboBox fromLng_tab1;
    private UIComboBox fromLat_tab1;
    private UIComboBox toArea_tab1;
    private UIComboBox toLng_tab1;
    private UIComboBox toLat_tab1;

    private LineMapDataSetFieldsPane lineMapDataSetFieldsPane;

    public LineMapAreaLngLatPaneWithComboBox(LineMapDataSetFieldsPane lineMapDataSetFieldsPane) {
        this.lineMapDataSetFieldsPane = lineMapDataSetFieldsPane;
        initComponents();
    }

    protected void initComponents() {
        if (lineMapDataSetFieldsPane == null) {
            return;
        }
        super.initComponents();
    }


    @Override
    protected JPanel createAreaPane() {
        if (fromArea_tab0 == null) {
            fromArea_tab0 = new UIComboBox();
            toArea_tab0 = new UIComboBox();
        }
        return createPane(
                new String[]{Toolkit.i18nText("Fine-Design_Chart_Start_Area_Name"),
                        Toolkit.i18nText("Fine-Design_Chart_End_Area_Name")},
                lineMapDataSetFieldsPane.createAreaPanel(fromArea_tab0), lineMapDataSetFieldsPane.createAreaPanel(toArea_tab0)
        );
    }

    @Override
    protected JPanel createAreaLngLatPane() {
        if (fromArea_tab1 == null) {
            fromArea_tab1 = new UIComboBox();
            fromLng_tab1 = new UIComboBox();
            fromLat_tab1 = new UIComboBox();
            toArea_tab1 = new UIComboBox();
            toLng_tab1 = new UIComboBox();
            toLat_tab1 = new UIComboBox();
        }
        return createPane(
                new String[]{
                        Toolkit.i18nText("Fine-Design_Chart_Start_Area_Name"),
                        Toolkit.i18nText("Fine-Design_Chart_Start_Longitude"),
                        Toolkit.i18nText("Fine-Design_Chart_Start_Latitude"),
                        Toolkit.i18nText("Fine-Design_Chart_End_Area_Name"),
                        Toolkit.i18nText("Fine-Design_Chart_End_Longitude"),
                        Toolkit.i18nText("Fine-Design_Chart_End_Latitude")},
                lineMapDataSetFieldsPane.createAreaPanel(fromArea_tab1),
                fromLng_tab1,
                fromLat_tab1,
                lineMapDataSetFieldsPane.createAreaPanel(toArea_tab1),
                toLng_tab1,
                toLat_tab1);
    }

    protected void populateTab0(LineMapColumnFieldCollection fieldCollection) {
        AbstractDataSetFieldsPane.populateField(fromArea_tab0, fieldCollection.getFromAreaName());
        AbstractDataSetFieldsPane.populateField(toArea_tab0, fieldCollection.getToAreaName());
    }

    protected void updateTab0(LineMapColumnFieldCollection fieldCollection) {
        AbstractDataSetFieldsPane.updateField(fromArea_tab0, fieldCollection.getFromAreaName());
        AbstractDataSetFieldsPane.updateField(toArea_tab0, fieldCollection.getToAreaName());
    }

    protected void populateTab1(LineMapColumnFieldCollection fieldCollection) {
        AbstractDataSetFieldsPane.populateField(fromArea_tab1, fieldCollection.getFromAreaName());
        AbstractDataSetFieldsPane.populateField(toArea_tab1, fieldCollection.getToAreaName());
        AbstractDataSetFieldsPane.populateField(fromLng_tab1, fieldCollection.getFromLng());
        AbstractDataSetFieldsPane.populateField(toLng_tab1, fieldCollection.getToLng());
        AbstractDataSetFieldsPane.populateField(fromLat_tab1, fieldCollection.getFromLat());
        AbstractDataSetFieldsPane.populateField(toLat_tab1, fieldCollection.getToLat());
    }

    protected void updateTab1(LineMapColumnFieldCollection fieldCollection) {
        AbstractDataSetFieldsPane.updateField(fromArea_tab1, fieldCollection.getFromAreaName());
        AbstractDataSetFieldsPane.updateField(toArea_tab1, fieldCollection.getToAreaName());
        AbstractDataSetFieldsPane.updateField(fromLng_tab1, fieldCollection.getFromLng());
        AbstractDataSetFieldsPane.updateField(toLng_tab1, fieldCollection.getToLng());
        AbstractDataSetFieldsPane.updateField(fromLat_tab1, fieldCollection.getFromLat());
        AbstractDataSetFieldsPane.updateField(toLat_tab1, fieldCollection.getToLat());
    }

    public UIComboBox[] allFieldComboBox() {
        return new UIComboBox[]{
                fromArea_tab0,
                toArea_tab0,
                fromArea_tab1,
                fromLng_tab1,
                fromLat_tab1,
                toArea_tab1,
                toLng_tab1,
                toLat_tab1
        };
    }
}
