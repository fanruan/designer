package com.fr.design.mainframe.widget.editors;

import java.text.NumberFormat;

public class LongEditor extends FormattedEditor {

    public LongEditor() {
        super(NumberFormat.getIntegerInstance());
    }

    @Override
    public Object getValue() {
        Object v = super.getValue();
        if (v == null) {
            return new Long(0);
        } else if (v instanceof Number) {
            return new Long(((Number) v).longValue());
        }
        return v;
    }
}