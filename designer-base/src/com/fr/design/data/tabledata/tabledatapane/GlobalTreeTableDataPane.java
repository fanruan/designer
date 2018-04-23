package com.fr.design.data.tabledata.tabledatapane;

import com.fr.design.data.datapane.GlobalTreeTableDataDictPane;


/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-4-20
 * Time: 上午11:30
 * To change this template use File | Settings | File Templates.
 */
public class GlobalTreeTableDataPane extends TreeTableDataPane {
    public GlobalTreeTableDataPane(String treeName){
        this.treeTableDataDictPane = new GlobalTreeTableDataDictPane(treeName);
        initComponent();

    }
}