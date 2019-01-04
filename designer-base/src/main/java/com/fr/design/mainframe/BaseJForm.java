package com.fr.design.mainframe;

import com.fr.form.FormElementCaseContainerProvider;

import javax.swing.JComponent;


/**
 * Author : Shockway
 * Date: 13-7-15
 * Time: 上午10:28
 */
public interface BaseJForm<T> extends JTemplateProvider<T> {

    String XML_TAG = "JForm";
    int FORM_TAB = 0;
    int ELEMENTCASE_TAB = 1;
    int ELEMENTCASE_CHANGE_TAB = 2;

    /**
     * 刷新所有控件
     */
    void refreshAllNameWidgets();

    /**
     * 刷新参数
     */
    void populateParameter();

    /**
     * 刷新选中的控件
     */
    void refreshSelectedWidget();

    /**
     * 执行撤销
     *
     * @param o 之前保存的状态
     */
    void applyUndoState4Form(BaseUndoState o);

    /**
     * 获取当前编辑的组件
     */
    JComponent getEditingPane();

    /**
     * 只在Form和ElementCase之间切换
     *
     * @param index 切换位置
     */
    void tabChanged(int index);

    /**
     * 在Form和ElementCase, 以及ElementCase和ElementCase之间切换
     *
     * @param index       切换位置
     * @param ecContainer ElementCase所在container
     */
    void tabChanged(int index, FormElementCaseContainerProvider ecContainer);
}