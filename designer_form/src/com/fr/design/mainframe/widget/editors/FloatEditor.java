package com.fr.design.mainframe.widget.editors;

import java.text.NumberFormat;

public class FloatEditor extends FormattedEditor {

    public FloatEditor() {
        super(NumberFormat.getNumberInstance());
    }

    @Override
    public Object getValue() {
        Object v = super.getValue();
        if (v == null) {
            return new Float(0);
        } else if (v instanceof Number) {
            return new Float(((Number) v).floatValue());
        }
        return v;
    }
}