package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.PointMapColumnFieldCollection;
import com.fr.design.chartx.data.map.PointMapAreaLngLatPaneWithComboBox;
import com.fr.design.gui.icombobox.UIComboBox;

import javax.swing.JPanel;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/8
 */
public class PointMapDataSetFieldsPane extends AbstractDataSetFieldsWithSeriesValuePane<PointMapColumnFieldCollection> {
    private PointMapAreaLngLatPaneWithComboBox areaLngLatPane;

    @Override
    protected JPanel createNorthPane() {
        if (areaLngLatPane == null) {
            areaLngLatPane = new PointMapAreaLngLatPaneWithComboBox();
        }
        return areaLngLatPane;
    }

    @Override
    protected String[] fieldLabels() {
        return new String[0];
    }

    @Override
    protected UIComboBox[] filedComboBoxes() {
        if (areaLngLatPane == null) {
            areaLngLatPane = new PointMapAreaLngLatPaneWithComboBox();
        }
        return areaLngLatPane.allFieldComboBox();
    }

    @Override
    public void populateBean(PointMapColumnFieldCollection ob) {
        areaLngLatPane.populate(ob);
        populateSeriesValuePane(ob);
    }

    @Override
    public PointMapColumnFieldCollection updateBean() {
        PointMapColumnFieldCollection fieldCollection = new PointMapColumnFieldCollection();
        areaLngLatPane.update(fieldCollection);
        updateSeriesValuePane(fieldCollection);
        return fieldCollection;
    }
}
