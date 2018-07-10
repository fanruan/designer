package com.fr.design.mainframe.widget.editors;

import java.text.NumberFormat;

public class DoubleEditor extends FormattedEditor {

    public DoubleEditor() {
        super(NumberFormat.getNumberInstance());
    }

    @Override
    public Object getValue() {
        Object v = super.getValue();
        if (v == null) {
            return new Double(0);
        } else if (v instanceof Number) {
            return new Double(((Number) v).doubleValue());
        }
        return v;
    }
}