package com.fr.design.data.tabledata.tabledatapane;

import com.fr.base.TableData;
import com.fr.design.data.tabledata.StoreProcedureWorkerListener;
import com.fr.design.beans.BasicBeanPane;

public abstract class AbstractTableDataPane<T extends TableData> extends BasicBeanPane<T> {

    public void addStoreProcedureWorkerListener(StoreProcedureWorkerListener listener) {
    }

    public void removeStoreProcedureWorkerListener() {
    }

}