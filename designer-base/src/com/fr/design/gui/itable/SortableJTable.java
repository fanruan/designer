package com.fr.design.gui.itable;

import javax.swing.JTable;

public class SortableJTable extends JTable{
    public SortableJTable(TableSorter tableModel){
        super(tableModel);
        tableModel.setTableHeader(getTableHeader());
    }
}