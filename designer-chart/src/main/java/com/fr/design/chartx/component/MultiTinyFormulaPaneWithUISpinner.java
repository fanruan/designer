package com.fr.design.chartx.component;

import com.fr.chartx.data.field.ColumnField;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.formula.TinyFormulaPane;

/**
 * Created by shine on 2019/6/19.
 */
public class MultiTinyFormulaPaneWithUISpinner extends AbstractMultiComponentPaneWithUISpinner<TinyFormulaPane> {
    @Override
    protected TinyFormulaPane createJComponent() {
        return new TinyFormulaPane();
    }

    @Override
    protected void populateField(TinyFormulaPane component, ColumnField field) {
        AbstractCellDataFieldsPane.populateField(component, field);
    }

    @Override
    protected void updateField(TinyFormulaPane component, ColumnField field) {
        AbstractCellDataFieldsPane.updateField(component, field);
    }
}
