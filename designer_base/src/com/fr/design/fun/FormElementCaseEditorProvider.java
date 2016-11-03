package com.fr.design.fun;

import com.fr.stable.fun.IOFileAttrMark;
import com.fr.stable.fun.ReportFitAttrProvider;
import com.fr.stable.fun.mark.Mutable;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * Created by zhouping on 2015/9/10.
 */
public interface FormElementCaseEditorProvider extends Mutable {

    String MARK_STRING = "PropertyEditor";

    int CURRENT_LEVEL = 1;

    PropertyDescriptor[] createPropertyDescriptor (Class<?> temp, Map<String, IOFileAttrMark> attrMarkMap, ReportFitAttrProvider reportFitAttr);

    /**
     * 生成属性表
     * @param temp 传入当前操作的class
     * @param reportFitAttr 传入的自适应属性
     * @return 返回属性表
     */
    PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, ReportFitAttrProvider reportFitAttr);

    /**
     * 返回pc自适应属性值
     * @param fitAttrProvider 传入的自适应属性
     * @return 返回pc自适应属性值
     */
    int getFitStateInPC(ReportFitAttrProvider fitAttrProvider);
}