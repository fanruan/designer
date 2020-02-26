package com.fr.design.chartx.component;

import com.fr.chartx.data.field.ColumnField;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.formula.TinyFormulaPane;

/**
 * Created by shine on 2019/4/12.
 */
public class MultiTinyFormulaPane extends AbstractMultiComponentPane<TinyFormulaPane> {

    @Override
    protected int componentWidth() {
        return 116;
    }

    @Override
    protected TinyFormulaPane createFirstFieldComponent() {
        return new TinyFormulaPane();
    }

    @Override
    protected TinyFormulaPane createOtherFieldComponent() {
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