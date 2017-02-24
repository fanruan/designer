package com.fr.design.fun;

import com.fr.form.ui.Widget;
import com.fr.stable.fun.mark.Mutable;

import javax.swing.*;
import java.awt.*;

/**
 * 报表工具栏设计器端拓展，用于配置按钮额外属性
 * Created by harry on 2016-12-23.
 */
public interface ExtraButtonToolBarProvider extends Mutable {

    String XML_TAG = "ExtraButtonToolBarProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 用于添加属性面板
     *
     * @param centerPane 面板
     */
    void updateCenterPane(JPanel centerPane);

    /**
     * 更新界面
     *
     * @param widget     控件
     * @param card       卡片布局
     * @param centerPane 面板
     */
    void populate(Widget widget, CardLayout card, JPanel centerPane);

    /**
     * 保存界面设置
     *
     * @param widget 控件
     */
    void update(Widget widget);
}
