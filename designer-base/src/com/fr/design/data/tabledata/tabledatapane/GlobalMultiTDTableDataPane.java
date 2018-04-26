package com.fr.design.data.tabledata.tabledatapane;

import com.fr.design.data.DesignTableDataManager;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-4-20
 * Time: 上午10:10
 * To change this template use File | Settings | File Templates.
 */
public class GlobalMultiTDTableDataPane extends MultiTDTableDataPane {

    public GlobalMultiTDTableDataPane(String string){

        super(string);
    }
    protected void setResMap() {
        resMap = DesignTableDataManager.getGlobalDataSet();

    }
}