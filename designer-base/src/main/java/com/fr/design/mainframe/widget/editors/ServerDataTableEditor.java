package com.fr.design.mainframe.widget.editors;

import com.fr.data.AbstractTableDataSource;
import com.fr.data.TableDataSource;

public class ServerDataTableEditor extends DataTableEditor {
    private static final AbstractTableDataSource F = new AbstractTableDataSource() {};

    @Override
    protected TableDataSource getTableDataSource() {
        return F;
    }
}