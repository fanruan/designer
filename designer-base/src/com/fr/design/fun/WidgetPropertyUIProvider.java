package com.fr.design.fun;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.stable.fun.mark.Mutable;


/**
 * Created by vito on 16/4/27.
 */
public interface WidgetPropertyUIProvider extends Mutable {
    String XML_TAG = "WidgetPropertyUIProvider";

    int CURRENT_LEVEL = 1;

    @Deprecated
    AbstractPropertyTable createWidgetAttrTable();

    //fanglei: 9.0新界面采用pane，不再用JTable
    BasicPane createWidgetAttrPane();

    String tableTitle();
}
