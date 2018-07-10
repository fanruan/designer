package com.fr.design.widget;

import com.fr.design.beans.BasicBeanPane;
import com.fr.form.ui.Widget;

/**
 * 格子中控件的设计界面
 */
public class Appearance {

    public static final String P_MARK = "Mark100";

    private Class<? extends BasicBeanPane<? extends Widget>> defineClass;
    private String displayName;

    /**
     * 构造函数，构造出用于控件界面
     * @param defineClass 构造该设计界面的类
     * @param displayName 显示值
     */
    public Appearance(Class<? extends BasicBeanPane<? extends Widget>> defineClass, String displayName) {
        this.defineClass = defineClass;
        this.displayName = displayName;
    }

    public Class<? extends BasicBeanPane<? extends Widget>> getDefineClass() {
        return defineClass;
    }

    public String getDisplayName() {
        return displayName;
    }
}