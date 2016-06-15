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
     * 生成属性表
     * @param temp 传入当前操作的class
     * @return 返回属性表
     */
    @Override
    public PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, ReportFitAttrProvider formFitAttr, ReportFitAttrProvider elementcaseFitAttr) {
        return new PropertyDescriptor[0];
    }

    @Override
    public int getFitStateInPC(ReportFitAttrProvider fitAttrProvider) {
        return 0;
    }
}