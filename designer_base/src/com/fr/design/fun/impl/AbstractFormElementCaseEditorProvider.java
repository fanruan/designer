package com.fr.design.fun.impl;

import com.fr.design.fun.FormElementCaseEditorProvider;
import com.fr.stable.fun.IOFileAttrMark;
import com.fr.stable.fun.ReportFitAttrProvider;
import com.fr.stable.fun.mark.API;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * Created by zhouping on 2015/9/10.
 */
@API(level = FormElementCaseEditorProvider.CURRENT_LEVEL)
public abstract class AbstractFormElementCaseEditorProvider implements FormElementCaseEditorProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return this.getClass().getName();
    }

    @Override
    public PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, Map<String, IOFileAttrMark> attrMarkMap, ReportFitAttrProvider reportFitAttr) {
        return new PropertyDescriptor[0];
    }

    /**
     * 生成属性表
     * @param temp 传入当前操作的class
     * @param reportFitAttr 传入的自适应属性
     * @return 返回属性表
     */
    @Override
    public PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, ReportFitAttrProvider reportFitAttr) {
        return new PropertyDescriptor[0];
    }

    /**
     * 返回pc自适应属性值
     * @param fitAttrProvider 传入的自适应属性
     * @return 返回pc自适应属性值
     */
    @Override
    public int getFitStateInPC(ReportFitAttrProvider fitAttrProvider) {
        return 0;
    }
}