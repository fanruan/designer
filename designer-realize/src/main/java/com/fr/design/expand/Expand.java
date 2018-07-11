/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.expand;

import com.fr.stable.ColumnRow;

/**
 * @author richer
 * @since 6.5.3
 */
public class Expand {
    private boolean _default;
    private ColumnRow columnRow;

    public Expand(){
        
    }

    public Expand(boolean _default, ColumnRow columnRow) {
        this._default = _default;
        this.columnRow = columnRow;
    }

    public boolean isDefault() {
        return this._default;
    }

    public void setDefault(boolean _default) {
        this._default = _default;
    }
    
    public ColumnRow getParentColumnRow() {
        return this.columnRow;
    }

    public void setParentColumnRow(ColumnRow columnRow) {
        this.columnRow = columnRow;
    }
}