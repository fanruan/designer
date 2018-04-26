package com.fr.design.data.datapane;

import com.fr.design.data.DesignTableDataManager;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-4-20
 * Time: 上午11:27
 * To change this template use File | Settings | File Templates.
 */
public class GlobalTreeTableDataDictPane extends  TreeTableDataDictPane {

    public GlobalTreeTableDataDictPane(String treeName) {
        super(treeName);
    }

    protected void setTableDataNameComboBox(String string) {
        tableDataNameComboBox = new GlobalTableDataComboBox(DesignTableDataManager.getEditingTableDataSource(),string);
    }
}