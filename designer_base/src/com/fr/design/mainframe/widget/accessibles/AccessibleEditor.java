package com.fr.design.mainframe.widget.accessibles;

import java.awt.Component;

import javax.swing.event.ChangeListener;

import com.fr.design.Exception.ValidationException;

/**
 * 属性编辑器
 * @since 6.5.2
 */
public interface AccessibleEditor {

    /**
     * 判断输入的值是否符合要求
     * @throws ValidationException
     */
    public void validateValue() throws ValidationException;

    /**
     * 获取编辑器里面的值
     */
    public Object getValue();

    /**
     * 给编辑器设置值
     * @param v
     */
    public void setValue(Object v);

    /**
     * 自定义编辑器
     * @return
     */
    public Component getEditor();

    public void addChangeListener(ChangeListener l);

    public void removeChangeListener(ChangeListener l);
}