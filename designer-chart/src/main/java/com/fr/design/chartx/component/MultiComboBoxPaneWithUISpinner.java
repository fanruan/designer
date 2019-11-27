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

    private boolean hasUse = false;

    @Override
    protected void initComps() {
        currentBoxList = new ArrayList();
        super.initComps();
    }

    public void setCurrentBoxList(List currentBoxList) {
        this.currentBoxList = currentBoxList;
    }

    public void setHasUse(boolean hasUse) {
        this.hasUse = hasUse;
    }

    @Override
    protected UIComboBox createJComponent() {
        UIComboBox uiComboBox = new UIComboBox(currentBoxList.toArray(new Object[currentBoxList.size()]));
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
