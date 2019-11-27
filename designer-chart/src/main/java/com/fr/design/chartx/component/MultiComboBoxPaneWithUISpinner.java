package com.fr.design.chartx.component;

import com.fr.chartx.data.field.ColumnField;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;
import com.fr.design.gui.icombobox.UIComboBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shine on 2019/6/18.
 */
public class MultiComboBoxPaneWithUISpinner extends AbstractMultiComponentPaneWithUISpinner<UIComboBox> {
    private List currentBoxList = new ArrayList();

    @Override
    protected void initComps() {
        currentBoxList = new ArrayList();
        super.initComps();
    }

    public void setCurrentBoxList(List currentBoxList) {
        this.currentBoxList = currentBoxList;
    }

    @Override
    protected UIComboBox createJComponent() {
        return new UIComboBox(currentBoxList.toArray(new Object[currentBoxList.size()]));
    }

    @Override
    protected void populateField(UIComboBox component, ColumnField field) {
        AbstractDataSetFieldsPane.populateField(component, field);
    }

    @Override
    protected void updateField(UIComboBox component, ColumnField field) {
        AbstractDataSetFieldsPane.updateField(component, field);
    }
}
