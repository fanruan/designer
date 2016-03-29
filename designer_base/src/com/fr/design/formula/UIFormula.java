package com.fr.design.formula;

import com.fr.base.Formula;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionListener;

import java.awt.*;

/**
 * @author richie
 * @date 2015-06-24
 * @since 8.0
 */
public interface UIFormula {

    /**
     * 从已有的公式还原公式面板
     * @param formula 公式
     */
    void populate(Formula formula);

    /**
     * 根据指定的变量处理和公式还原公式面板
     * @param formula 公式
     * @param variableResolver 变量处理器
     */
    void populate(Formula formula, VariableResolver variableResolver);

    /**
     * 获取公式面板的参数
     * @return 公式
     */
    Formula update();

    /**
     * 显示窗口
     * @param window 窗口
     * @param l 对话框监听器
     * @return 对话框
     */
    BasicDialog showLargeWindow(Window window, DialogActionListener l);
}