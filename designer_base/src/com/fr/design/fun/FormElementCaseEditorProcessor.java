package com.fr.design.fun;

import com.fr.stable.fun.Level;
import com.fr.stable.fun.ReportFitAttrProvider;

import java.beans.PropertyDescriptor;

/**
 * Created by zhouping on 2015/9/10.
 */
public interface FormElementCaseEditorProcessor extends Level{

    String MARK_STRING = "PropertyEditor";

    int CURRENT_LEVEL = 1;


    /**
     * 生成属性表
     * @param temp 传入当前操作的class
     * @return 返回属性表
     */
    PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, ReportFitAttrProvider formFitAttr, ReportFitAttrProvider elementcaseFitAttr);

    int getFitStateInPC(ReportFitAttrProvider fitAttrProvider);


}