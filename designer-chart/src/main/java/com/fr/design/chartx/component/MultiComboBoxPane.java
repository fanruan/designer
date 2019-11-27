package com.fr.design.chartx.component;

import com.fr.chartx.data.field.ColumnField;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.extended.chart.UIComboBoxWithNone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shine on 2019/4/12.
 */
public class MultiComboBoxPane extends AbstractMultiComponentPane<UIComboBox> {
    private List currentBoxItems = new ArrayList();

    private boolean hasUse = false;

    public void setCurrentBoxItems(List currentBoxItems) {
        this.currentBoxItems = currentBoxItems;
    }

    public void setHasUse(boolean hasUse) {
        this.hasUse = hasUse;
    }

    @Override
    protected UIComboBox createFirstFieldComponent() {
        return new UIComboBoxWithNone();
    }

    @Override
    protected UIComboBox createOtherFieldComponent() {
        UIComboBox uiComboBox = new UIComboBox(currentBoxItems.toArray(new Object[currentBoxItems.size()]));
        uiComboBox.setEnabled(hasUse);
        return uiComboBox;
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
