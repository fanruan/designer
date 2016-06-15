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
     * @param formFitAttr 表单自适应属性
     * @param elementcaseFitAttr 报表块自适应属性
     * @return 返回属性表
     */
    PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, ReportFitAttrProvider formFitAttr, ReportFitAttrProvider elementcaseFitAttr);

    /**
     * 返回pc自适应属性值
     * @param fitAttrProvider 传入的自适应属性
     * @return 返回pc自适应属性值
     */
    int getFitStateInPC(ReportFitAttrProvider fitAttrProvider);


}