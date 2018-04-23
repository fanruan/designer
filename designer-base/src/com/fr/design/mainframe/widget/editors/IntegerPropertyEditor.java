package com.fr.design.mainframe.widget.editors;

import java.text.NumberFormat;

public class IntegerPropertyEditor extends FormattedEditor {

    public IntegerPropertyEditor() {
        super(NumberFormat.getIntegerInstance());
    }

    @Override
    public Object getValue() {
        Object v = super.getValue();
        if (v == null) {
            return 0;
        }
        return ((Number) v).intValue();
    }
}