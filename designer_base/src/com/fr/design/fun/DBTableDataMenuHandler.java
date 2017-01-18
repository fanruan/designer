package com.fr.design.fun;

import com.fr.data.impl.DBTableData;
import com.fr.design.actions.UpdateAction;
import com.fr.stable.fun.mark.Immutable;

/**
 * Created by xiaxiang on 2017/1/15.
 */
public interface DBTableDataMenuHandler extends Immutable {
    String MARK_STRING = "DBTableDataMenuHandler";

    int CURRENT_LEVEL = 1;

    UpdateAction createQueryAction();

    void populate(DBTableData dbTableData);

    DBTableData update();


}
