package com.fr.design.fun;

import com.fr.form.ui.Widget;
import com.fr.stable.fun.mark.Immutable;

/**
 * Coder: zack
 * Date: 2016/5/12
 * Time: 10:37
 */
public interface WidgetDesignHandler extends Immutable {
    String XML_TAG = "WidgetDesignHandler";
    int CURRENT_LEVEL = 1;

    /**
     * 传递控件共有属性
     *
     * @param oldWidget 老控件
     * @param newWidget 新控件
     */
    void transferWidgetProperties(Widget oldWidget, Widget newWidget);
}
