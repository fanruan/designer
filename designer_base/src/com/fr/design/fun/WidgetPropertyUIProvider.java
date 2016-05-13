package com.fr.design.fun;

import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.stable.fun.Level;


/**
 * Created by vito on 16/4/27.
 */
public interface WidgetPropertyUIProvider extends Level {
    String XML_TAG = "WidgetPropertyUIProvider";

    int CURRENT_LEVEL = 1;

    AbstractPropertyTable createWidgetAttrTable();

    String tableTitle();
}
