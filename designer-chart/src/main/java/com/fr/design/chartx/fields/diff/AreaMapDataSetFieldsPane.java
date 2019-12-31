package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.AreaMapColumnFieldCollection;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.i18n.Toolkit;
import com.fr.plugin.chart.type.MapType;

import java.awt.Component;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/7
 */
public class AreaMapDataSetFieldsPane extends MapDataSetFieldsPane<AreaMapColumnFieldCollection> {

    private UIComboBox areaName;

    @Override
    protected String[] fieldLabels() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Area_Name")
        };
    }

    @Override
    protected UIComboBox[] filedComboBoxes() {
        return new UIComboBox[]{
                createAreaName()
        };
    }

    @Override
    protected Component[] fieldComponents() {
        return new Component[]{
                createAreaPanel(createAreaName())
        };
    }

    private UIComboBox createAreaName() {
        if (areaName == null) {
            areaName = new UIComboBox();
        }
        return areaName;
    }

    @Override
    public void populateBean(AreaMapColumnFieldCollection ob) {
        populateField(areaName, ob.getAreaName());
        populateSeriesValuePane(ob);
    }

    @Override
    public AreaMapColumnFieldCollection updateBean() {
        AreaMapColumnFieldCollection fieldCollection = new AreaMapColumnFieldCollection();
        updateField(areaName, fieldCollection.getAreaName());
        updateSeriesValuePane(fieldCollection);
        return fieldCollection;
    }

    public MapType getMapType() {
        return MapType.AREA;
    }
}
