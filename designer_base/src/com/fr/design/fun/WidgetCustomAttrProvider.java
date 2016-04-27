package com.fr.design.fun;

import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.stable.fun.Level;


/**
 * Created by vito on 16/4/27.
 */
public interface WidgetCustomAttrProvider extends Level {
    String XML_TAG = "WidgetCustomAttrProvider";

    int CURRENT_LEVEL = 1;

    AbstractPropertyTable createWidgetCustomAttrTable();

    String setTableTitle();
}
