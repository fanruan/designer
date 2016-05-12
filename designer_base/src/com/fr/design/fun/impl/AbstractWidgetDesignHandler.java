package com.fr.design.fun.impl;

import com.fr.design.fun.WidgetDesignHandler;
import com.fr.form.ui.Widget;

/**
 * Coder: zack
 * Date: 2016/5/12
 * Time: 10:41
 */
public abstract class AbstractWidgetDesignHandler implements WidgetDesignHandler {
    @Override
    public Widget dealWithWidget(Widget oldWidget, Widget newWidget) {
        return newWidget;
    }
}
