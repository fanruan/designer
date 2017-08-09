package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.designer.properties.Encoder;

public abstract class UneditableAccessibleEditor extends BaseAccessibleEditor {

    protected Object value;

    public UneditableAccessibleEditor(Encoder enc) {
        super(enc, null, true);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object v) {
        this.value = v;
        super.setValue(v);
    }
}