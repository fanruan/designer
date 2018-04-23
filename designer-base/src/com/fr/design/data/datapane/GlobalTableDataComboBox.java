package com.fr.design.data.datapane;

import com.fr.design.data.DesignTableDataManager;
import com.fr.data.TableDataSource;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-4-20
 * Time: 上午11:16
 * To change this template use File | Settings | File Templates.
 */
public class GlobalTableDataComboBox extends TableDataComboBox {

    public GlobalTableDataComboBox(TableDataSource source, String treeName) {
        super(source, treeName);
    }
    protected void setResMap(TableDataSource source){
        resMap = DesignTableDataManager.getGlobalDataSet();

    }
}