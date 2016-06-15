package com.fr.design.fun.impl;

import com.fr.design.fun.FormElementCaseEditorProcessor;
import com.fr.stable.fun.ReportFitAttrProvider;

import java.beans.PropertyDescriptor;

/**
 * Created by zhouping on 2015/9/10.
 */
public abstract class AbstractFormElementCaseEditorProcessor implements FormElementCaseEditorProcessor {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }


    /**
     * 返回pc自适应属性值
     * @param temp 传入当前操作的class
     * @param formFitAttr 表单自适应属性
     * @param elementcaseFitAttr 报表块自适应属性
     * @return 返回属性表
     */
    @Override
    public PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, ReportFitAttrProvider formFitAttr, ReportFitAttrProvider elementcaseFitAttr) {
        return new PropertyDescriptor[0];
    }

    /**
     * 生成属性表
     * @param fitAttrProvider 传入的自适应属性
     * @return 返回pc自适应属性值
     */
    @Override
    public int getFitStateInPC(ReportFitAttrProvider fitAttrProvider) {
        return 0;
    }
}