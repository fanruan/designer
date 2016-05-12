package com.fr.design.fun;

import com.fr.form.ui.Widget;
import com.fr.stable.fun.Level;

/**
 * Coder: zack
 * Date: 2016/5/12
 * Time: 10:37
 */
public interface WidgetDesignHandler extends Level {
    String XML_TAG = "WidgetDesignHandler";
    int CURRENT_LEVEL = 1;
    /**
     * 控件设置的时候对原控件和新控件的一些特殊处理（比如属性的传递，默认属性的设置等）
     *
     * @param oldWidget 老控件
     * @param newWidget 新控件
     * @return 处理后的控件
     */
    Widget dealWithWidget(Widget oldWidget, Widget newWidget);
}
