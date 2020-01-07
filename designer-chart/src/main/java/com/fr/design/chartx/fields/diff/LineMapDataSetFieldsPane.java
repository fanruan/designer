package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.LineMapColumnFieldCollection;
import com.fr.design.chartx.data.map.LineMapAreaLngLatPaneWithComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.third.jodd.util.ArraysUtil;

import javax.swing.JPanel;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/11
 */
public class LineMapDataSetFieldsPane extends MapDataSetFieldsPane<LineMapColumnFieldCollection> {
    private LineMapAreaLngLatPaneWithComboBox areaLngLatPane;

    private UIComboBox lineName;

    @Override
    protected JPanel createNorthPane() {
        if (areaLngLatPane == null) {
            areaLngLatPane = new LineMapAreaLngLatPaneWithComboBox(this);
        }
        return areaLngLatPane;
    }

    @Override
    protected String[] fieldLabels() {
        return new String[]{
                "lineName"
        };
    }

    @Override
    protected UIComboBox[] filedComboBoxes() {
        if (lineName == null) {
            lineName = new UIComboBox();
        }
        UIComboBox[] result = areaLngLatPane.allFieldComboBox();
        return ArraysUtil.join(new UIComboBox[]{
                lineName
        }, result);
    }

    @Override
    public void populateBean(LineMapColumnFieldCollection ob) {
        areaLngLatPane.populate(ob);
        populateField(lineName, ob.getLineName());
        populateSeriesValuePane(ob);
    }

    @Override
    public LineMapColumnFieldCollection updateBean() {
        LineMapColumnFieldCollection columnFieldCollection = new LineMapColumnFieldCollection();
        areaLngLatPane.update(columnFieldCollection);
        updateField(lineName, columnFieldCollection.getLineName());
        updateSeriesValuePane(columnFieldCollection);
        return columnFieldCollection;
    }
}
