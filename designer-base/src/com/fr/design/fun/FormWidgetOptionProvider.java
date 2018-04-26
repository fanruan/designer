package com.fr.design.fun;

/**
 * @author richie
 * @date 2015-03-23
 * @since 8.0
 * 表单控件
 */
public interface FormWidgetOptionProvider extends ParameterWidgetOptionProvider {

    String XML_TAG = "FormWidgetOptionProvider";

    /**
     * 组件是否是布局容器
     * @return 是布局容器则返回true，否则返回false
     */
    boolean isContainer();

}